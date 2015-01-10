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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Editor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7794921541255885374L;

	public static final String appName = "Song Editor";
	private final String arduino_export_path = "/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/arduino_exports/";


	private Song song;
	private long editorTime;
	boolean isPlaying;
	GUI gui;
	Timer timer;

	public Editor(Song s) {		
		this.song = s;
		gui = new GUI(this);
		setEditorTime(0);

		gui.printCues();

		timer = new Timer(this);
	}

	void stopTimer() {
		isPlaying = false;
		timer.audioLine.stop();
		//set editor time to stopped time
		setEditorTime(timer.audioLine.getMicrosecondPosition() / 1000);
		System.out.println("Stopped time: " + editorTime);
	}

	void startTimer() {
		(new Thread(timer)).start();
	}


	void refresh() {
		//Get list of cues
		this.song.getCues();
	}

	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Holiday LX Editor");

		//Welcome screen
		//		Welcome w = new Welcome();
		boolean success = false;


		while(!success) {
			String[] buttons = {"Quit", "Open Song", "Create New Song" };
			int selection = JOptionPane.showOptionDialog(null, "Welcome to the app!", "Welcome", JOptionPane.DEFAULT_OPTION, 0, null, buttons, buttons[2]);

			if(selection==2) {
				//Create new song
				success = true;
			}
			else if(selection == 1) {
				//Open song
				if(!Editor.openFile()) {
					success = false;
				}
				else success = true;
				
			}
			else {
				//Exit app
				success = true;
				System.exit(0);
			}
		}

		//		Song s = new Song("Let It Go", "/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/audio/Let_It_Go.wav");

		//		Song s = w.getSong();
		//
		//		s.addChannel(new Channel("White Tree", 1, 8));
		//		s.addChannel(new Channel("Blue Tree", 2, 9));
		//		s.addChannel(new Channel("Blues", 3, 3));
		//		s.addChannel(new Channel("Whites", 4, 5));
		//		s.addChannel(new Channel("Wreaths", 5, 2));
		//
		//		Editor e = new Editor(s);

		//		e.gui.printCues();

	}

	public boolean writeFile(){


		try {
			File file = new File(arduino_export_path + song.getTitle());
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
					return false;
				}
			}
			file = new File(arduino_export_path + song.getTitle() +"/"+song.getTitle()+".txt");

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			Object[] chs = song.getChannels();

			//Defines
			Arduino.writeDefines(bw, chs);

			//Intro comment
			Arduino.writeIntro(bw);

			//Setup method
			Arduino.writeSetup(bw, chs);

			//Begin Loop()
			bw.append("void loop() {\n");

			//Begin countdown
			Arduino.writeCountdown(bw, 10);

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
					for(int j=0; j<c.getEvents().size(); j++) {
						LightEvent e = c.getEvents().get(j);
						Arduino.digitalWrite(bw, e.getChannel(), e.isOn());
						Arduino.printToSerial(bw, "Cue: " + c.toString());


						if(e.isOn() && e.isEffect()){
							//Add to active effects
							activeEffects.add(new ActiveEffect(e, c.getRunTime()));
						}
						else {
							//Check if channel is on active effects, if so remove
							for (int k=0; k<activeEffects.size(); k++) {
								if(activeEffects.get(k).event.getChannel() == e.getChannel()) {
									activeEffects.remove(k);
									break;
								}
							}
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
							assert lastRunTime < 100000;
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

	public long getEditorTime() {
		return editorTime;
	}

	public void setEditorTime(long editorTime) {
		this.editorTime = editorTime;
	}

	public void editCue(Cue c) {
		//Handle button click to add new cue
		CuePane cp = new CuePane(this, c);
		Cue editedCue = cp.getCue();


		if (editedCue != null) {
			//remove current cue, add edited cue
			if(song.removeCue(c)) {
				System.out.println("Cue removed at " + c.getRunTime());

				if(song.addCue(editedCue)) {
					System.out.println("Cue added at " + editedCue.getRunTime());
				}
				else{
					System.err.println("Error edadding cue at " + editedCue.getRunTime());
				}
			}
			else{
				System.err.println("Error removing cue at " + c.getRunTime());
			}
		}
		gui.printCues();
	}

	public void addNewCue() {
		//Handle button click to add new cue
		Cue tmp = new Cue(editorTime);
		tmp.addEvent(new LightEvent(song.getChannels()[0], true, false, 0));

		CuePane cp = new CuePane(this, tmp);
		Cue newCue = cp.getCue();

		if(newCue != null) {
			if(song.addCue(newCue)) {
				System.out.println("Cue added at " + newCue.getRunTime());
			}
			else {
				System.err.println("Error adding cue at " + newCue.getRunTime());
			}

		}
		gui.printCues();

		//		if (newCuePane()){
		//			gui.printCues();
		//		}
		//		else {
		//			addNewCue();
		//		}
	}

	public boolean removeCue(Cue c) {
		if (song.removeCue(c)) {
			gui.printCues();
			return true;
		}
		else return false;

	}

	public void createNewFile(String fileName, String audioPath) {
		Song newSong = new Song(fileName, audioPath);
		newSong.copySong(this.song);
		Editor newEditor = new Editor(newSong);
		newEditor.saveFile();
		gui.f.setVisible(false);
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

	public static boolean openFile() {
		System.out.println("Opening");
		JFileChooser fc = new JFileChooser("/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator");


		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Ser", "ser");
		fc.setFileFilter(filter);					
		int opened = fc.showDialog(null, "Open");
		if (opened == JFileChooser.APPROVE_OPTION) {

			try {
				FileInputStream fin = new FileInputStream(fc.getSelectedFile());
				ObjectInputStream ois = new ObjectInputStream(fin);
				Song openSong = (Song) ois.readObject();
				Editor newEditor = new Editor(openSong);
				ois.close();
				return true;

			}
			catch(ClassNotFoundException e){
				System.err.println("File Not A Song File");
				JOptionPane.showConfirmDialog(null, "Error: File is not a song file.", "Invalid File", JOptionPane.DEFAULT_OPTION);
				return false;
			}

			catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}

		return false;
	}

	public void resetTimer() {
		timer.reset();
	}		
}
