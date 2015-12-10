import java.awt.Color;
import java.awt.Dialog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Editor {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7794921541255885374L;

	public static String appName;
	private String arduino_export_path;
	private LightsEditorApplication app;
	private Song song;
	private double editorTime;
	private boolean isPlaying;
	GUI gui;
	Timer timer;
	private Cue currentCue;
	private Cue nextCue;
	private EffectsTimer effectsTimer;

	private boolean showLive;

	private Cue selectedCue;

	public Editor(Song s, LightsEditorApplication application) {		
		this.song = s;
		this.app = application;
		
		//Set config values
		Editor.appName = app.getAppName();
		this.arduino_export_path = app.getArduinoExportPath();
		
		//Set current and next cue for playback
		//		this.currentCue = s.getCueList().get(0); first cue should be null until reached in plaback
		this.currentCue = null;
		if(s.getCueList().size() > 0) {
			this.nextCue = s.getCueList().get(0);
		}
		else this.nextCue = null;

		//Instantiate GUI
		gui = new GUI(this);
		//		setEditorTime(0); would like timer to call

		gui.printCues();

		timer = new Timer(this);
	}

	void stopTimer() {
		isPlaying = false;
		timer.audioLine.stop(); //Called directly due to threading issue		


		//set editor time to stopped time
		setEditorTime((timer.audioLine.getMicrosecondPosition() / 1000) + timer.resetOffsetMillis);

		timer.stopAudio();	//Currently only prints time to console to confirm thread synchronization	
		gui.togglePlayButton();
	}

	void startTimer() {
		//		System.out.println("Starting new thread");

		(new Thread(timer)).start();
		//		System.out.println(Thread.activeCount());
		gui.togglePlayButton();
	}


	void refresh() {
		//Get list of cues
		this.song.getCues();
	}

	public boolean changeAudio() {
		File newAudio = app.promptAudioFile();
		if (newAudio != null) {
			//If new file was chosen, change file on song
			this.song.setAudioFile(newAudio);
			//Restart the timer
			timer = new Timer(this);
			setEditorTime(0);
			//			gui.updateTime();
			return true;
		}
		else return false;
	}



	public boolean writeFile(){


		try {
			String arduinoFileName = song.getTitle().replace(' ', '_');


			File file = new File(arduino_export_path + arduinoFileName);
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
					return false;
				}
			}
			file = new File(arduino_export_path + arduinoFileName +"/"+arduinoFileName+".ino");

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			Object[] chs = song.getChannels();

			//Defines
			Arduino.writeDefines(bw, chs);
			Arduino.writeSongGlobals(bw, song);

			//Intro comment
			Arduino.writeIntro(bw);

			//Setup method
			bw.append("void setup() {\n");
			Arduino.writeSetup(bw, chs);
			Arduino.writeSongSetup(bw, song);
			bw.append("}\n\n");

			
			//Begin Loop()
			bw.append("void loop() {\n");

			//Begin countdown
			Arduino.writeCountdown(bw, 5);

			Object[] qs = song.getCues();

			class ActiveEffect implements Comparable<ActiveEffect> {
				LightEvent event;
				double startTime;
				double nextRun;
				boolean lastStateOn;

				public ActiveEffect(LightEvent e, double startTime) {
					this.event = e;
					this.startTime = startTime;
					this.nextRun = startTime + e.getEffectRate();
					this.lastStateOn = true;
				}

				@Override
				public int compareTo(ActiveEffect o) {
					if(this.nextRun > o.nextRun) return 1;
					else if (this.nextRun < o.nextRun) return -1;
					else return 0;
				}
			}

			//Check if any cues at all
			if(qs.length >0){

				ArrayList<ActiveEffect> activeEffects = new ArrayList<ActiveEffect>(); //Store current running effects;


				//Insert first delay
				Cue c = (Cue) qs[0];				
				Arduino.writeDelay(bw, c.getRunTime());

				for(int i=0; i<qs.length; i++) {
					c = (Cue) qs[i];	
					Arduino.printToSerial(bw, "Cue: " + c.toString());

					for(int j=0; j<c.getEvents().size(); j++) {
						LightEvent e = c.getEvents().get(j);

						//Check if channel is on active effects, if so remove
						if(!e.isEffect()){
							for (int k=0; k<activeEffects.size(); k++) {
								if(activeEffects.get(k).event.getChannel() == e.getChannel()) {
									activeEffects.remove(k);
									break;
								}
							}
						}

						if(e.getState() == LightEvent.EFFECT_STATE){
							//Add to active effects
							activeEffects.add(new ActiveEffect(e, c.getRunTime()));
							Arduino.digitalWrite(bw, e.getChannel(), true);
						}
						else {
							//Write channel based on isOn
							Arduino.digitalWrite(bw, e.getChannel(), e.isOn());
						}

					}
					//Sort Active Effects
					Collections.sort(activeEffects);


					//After each cue, place a delay equal to difference in timing
					//Check if there is another cue
					Cue nextCue;

					if (i<qs.length-1) nextCue = (Cue) qs[i+1];
					else nextCue = null;

					double lastRunTime = c.getRunTime();

					//Compare timing of next effect with next cue
					//See if there are any effects (but must have one cue left to avoid infinite loop
					if(!activeEffects.isEmpty() && nextCue!=null) {
						ActiveEffect nextEffect = activeEffects.get(0);
						while((activeEffects.size() >0) && (nextEffect.nextRun <= nextCue.getRunTime())) {
							//Remove the next effect
							activeEffects.remove(0);

							//Print delay and write digitalwrite for effect
							Arduino.writeDelay(bw, nextEffect.nextRun - lastRunTime);	
							Arduino.digitalWrite(bw, nextEffect.event.getChannel(), !(nextEffect.lastStateOn));

							//Set lastRunTime and nextRun
							lastRunTime = nextEffect.nextRun;
							nextEffect.nextRun += nextEffect.event.getEffectRate();

							//Toggle lastState
							nextEffect.lastStateOn = !nextEffect.lastStateOn;

							//Add back to effects list and resort
							activeEffects.add(nextEffect);
							Collections.sort(activeEffects);

							//Get next effect in list
							nextEffect = activeEffects.get(0);
						}
					}

					//If there is another cue, write next delay
					if(nextCue != null) {
						Arduino.writeDelay(bw, nextCue.getRunTime() - lastRunTime);
					}
				}
			}
			else {
				System.err.println("No cues");
			}

			//End loop
			bw.append("}");


			bw.close();

			System.out.println("File Write Complete");

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Song getCurrentSong() {
		return song;
	}

	public double getEditorTime() {
		return editorTime;
	}

	//Will be called by timer thread
	public synchronized void setEditorTime(double editorTime) {
		this.editorTime = editorTime;
		
		//Invoke later is called on gui update to avoid crash. I believe this is due to competing threads.
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				//Update gui
				gui.updateTime();

				//Always look for next cue in case a cue was added before to change number
				int currentIndex = song.getCueList().indexOf(currentCue);
				if(currentIndex+1 == song.getCueList().size()) {
					nextCue = null;
				}

				else {
					nextCue = song.getCueList().get(currentIndex+1);


					//See if cue list should update
					if(getEditorTime() >= nextCue.getRunTime()) {
						//			gui.removeHighlightCue(currentCue);
						//			gui.highlightCue(nextCue);
						if(currentCue != null) currentCue.setActive(false);
						nextCue.setActive(true);
						currentCue = nextCue;
						gui.printCues();

						//If mode is show live, update states list.
						if(showLive()) {
							updateChDisplays();
							updateGUIEventPanel();
						}
					}
				}				
			}
		});
		
	}

	public void editCue(Cue c) {
		//Handle button click to add new cue
		CuePane cp = new CuePane(this, c, "Edit Cue");
		Cue editedCue = cp.getCue();


		if (editedCue != null) {
			//remove current cue, add edited cue
			if(song.removeCue(c)) {
				System.out.println("Cue removed at " + c.getRuntTimeInSecs());

				if(song.addCue(editedCue)) {
					System.out.println("Cue added at " + editedCue.getRuntTimeInSecs());
				}
				else{
					System.err.println("Error edadding cue at " + editedCue.getRuntTimeInSecs());
				}
			}
			else{
				System.err.println("Error removing cue at " + c.getRuntTimeInSecs());
			}
		}
		gui.printCues();
		
		//Select edited cue
		setSelectedCue(editedCue);
		gui.list.setSelectedValue(selectedCue, true);
		
		//Update displays
		updateChDisplays();
		updateGUIEventPanel();
	}

	public void addNewCue() {
		//Handle button click to add new cue
		Cue tmp = new Cue(editorTime);
		tmp.addEvent(new LightEvent(song.getChannels()[0], true, false, 0));

		CuePane cp = new CuePane(this, tmp, "Add Cue");
		Cue newCue = cp.getCue();

		if(newCue != null) {
			if(song.addCue(newCue)) {
				System.out.println("Cue added at " + newCue.getRuntTimeInSecs());
			}
			else {
				System.err.println("Error adding cue at " + newCue.getRuntTimeInSecs());
			}

		}
		//Call set editor time to refresh cue list and active cue
		setEditorTime(editorTime);

		//Update cue list
		gui.printCues();

	}

	public boolean removeCue(Cue c) {
		if (song.removeCue(c)) {
			gui.printCues();
			return true;
		}
		else return false;

	}


	public boolean saveFile() {
		try{
			FileOutputStream fout = new FileOutputStream(song.getFilePath());
			System.out.println("Saving file at: " + song.getFilePath());
			ObjectOutputStream oos = new ObjectOutputStream(fout);

			//Write song object
			oos.writeObject(song);

		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}


	public void resetTimer(double percent) {
		System.out.println(percent);
//		boolean valid = false;
//		String input;
//		double percent = -1; //Now being passed by gui
		//		while(!valid) {
		//			input = JOptionPane.showInputDialog(null, "Enter song percent (0->1)");
		//
		//			//Check if cancel wasn't chosen (i.e. input isn't null)
		//			if(input != null) {
		//				try{
		//					percent = Double.parseDouble(input);
		//					if(percent>=0 && percent <1) valid = true;
		//					else JOptionPane.showMessageDialog(null, "Invalid Input. Please enter a value between 0 and 1");
		//				}
		//				catch(NumberFormatException e) {
		//
		//					JOptionPane.showMessageDialog(null, "Invalid Input. Please enter a value between 0 and 1");
		//				}
		//			}
		//			//If cancel is chosen, leave method
		//			else return;
		//		}

		timer.reset(percent);

		//need to revisit these
		currentCue.setActive(false);
		System.out.println(editorTime);
		//Find nearest cue before new time
		currentCue = getActiveCue(editorTime);
		currentCue.setActive(true);

		//Get position of current cue
		int currentCueIndex = song.getCueList().indexOf(currentCue);
		//See if this is the last cue
		if(currentCueIndex < (song.getCueList().size()-1)) {
			this.nextCue = song.getCueList().get(currentCueIndex+1);
		}
		else this.nextCue = null;	

		//Update display if in live mode
		if(showLive()) {
			updateChDisplays();
		}
		gui.printCues();
	}

	public boolean showLive() {
		return showLive;
	}	

	public void setShowLive(boolean live) {
		this.showLive = live;
	}

	public void updateChDisplays() {
		if(showLive()) {
			song.setChStates(currentCue);
		}
		else song.setChStates(selectedCue);

		gui.printStates();
	}

	public Cue getSelectedCue() {
		return selectedCue;
	}

	public void setSelectedCue(Cue selectedCue) {
		this.selectedCue = selectedCue;
	}

	public void updateGUIEventPanel() {
		if(showLive()) {
			gui.updateEventPanel(currentCue);
		}
		else if(selectedCue != null) gui.updateEventPanel(selectedCue);

	}

	public synchronized boolean isPlaying() {
		return isPlaying;
	}

	public synchronized void setIsPlaying(boolean b) {
		this.isPlaying = b;
	}

	private Cue getActiveCue(double time) {
		if(song.getCueList().isEmpty()) return null;
		//If the there are no cues before time, return null
		else if (song.getCueList().get(0).getRunTime() > time) return null;
		else {
			Cue prevCue = song.getCueList().get(0);
			for(Cue cue: song.getCueList()) {
				if(cue.getRunTime() > time) break;
				else prevCue = cue;
			}
			return prevCue;
		}
	}

	public void openFile() {
		app.openFile();
	}

	public void openEffectsTimer() {
		effectsTimer = new EffectsTimer(this);
	}


}
