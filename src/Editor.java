import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Editor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7794921541255885374L;

	public static final String appName = "Song Editor";

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



		Song s = new Song("Let It Go", "/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/audio/Let_It_Go.wav");

		s.addChannel(new Channel("White Tree", 1, 8));
		s.addChannel(new Channel("Blue Tree", 2, 9));
		s.addChannel(new Channel("Blues", 3, 3));
		s.addChannel(new Channel("Whites", 4, 5));
		s.addChannel(new Channel("Wreaths", 5, 2));

		Editor e = new Editor(s);

		e.gui.printCues();

	}

	public boolean writeFile(){

		try {
			File file = new File(song.getTitle());
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			file = new File(song.getTitle() +"/"+song.getTitle()+".txt");

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


	public void addNewCue() {
		//Handle button click to add new cue
		if (newCuePane()){
			gui.printCues();
		}
		else {
			addNewCue();
		}
	}

	public boolean newCuePane() {		
		JTextField cueTime = new JTextField(5);

		JLabel feedback; //Only initilized if error needs to be given to user
		final JPanel cuePanel = new JPanel();

		cuePanel.setLayout(new BoxLayout(cuePanel, BoxLayout.Y_AXIS));
		//		final JPanel chPanel = new JPanel();
		//May not need

		final JScrollPane scp = new JScrollPane(cuePanel);
		scp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//Delete?
		//		chPanel.setLayout(new BoxLayout(chPanel, BoxLayout.X_AXIS));
		//		cuePanel.add(chPanel);		



		cueTime.setText("" + editorTime);

		//Create first panel
		JPanel firstPanel = new JPanel();
		firstPanel.add(new JLabel("Cue Time:"));
		firstPanel.add(cueTime);
		cuePanel.add(firstPanel);

		final ArrayList<eventInput> events = new ArrayList<eventInput>();

		//add initial event
		eventInput firstEvent = new eventInput(song);
		events.add(firstEvent);
		cuePanel.add(firstEvent.createChPanel());	

		JButton addEvent = new JButton("Add Channels");

		firstPanel.add(addEvent);

		addEvent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final eventInput event = new eventInput(song);

				events.add(event);

				cuePanel.add(event.createChPanel());

				event.rmvButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						cuePanel.remove(event.getChPanel());
						events.remove(event);
						scp.validate();

					}
				});

				scp.validate();
			}
		});

		JFrame cueFrame = new JFrame();
		cueFrame.setResizable(true);

		int result = JOptionPane.showConfirmDialog(null, scp, 
				"New Cue", JOptionPane.OK_CANCEL_OPTION);


		if (result == JOptionPane.OK_OPTION) {
			//Validate input
			boolean success = true;
			double qTime = -1;
			Cue tmp = null;
			try{
				qTime = Double.parseDouble(cueTime.getText());
				tmp = new Cue(qTime);

			}
			catch(Exception e) {
				success = false;
				System.err.println("Cannot parseDouble time");

			}

			if(qTime >=0 && success) {
				assert tmp != null;
				for(int i=0; i<events.size(); i++) {
					boolean on = true;
					boolean effect = false;					
					eventInput ei = events.get(i);
					int effectRate = ei.getEffectRate();


					if(ei.state.getSelectedItem().equals("Off")) on = false;

					//Check if effect
					if(ei.state.getSelectedItem().equals("Effect")) effect = true;


					if(events.get(i).channel.getSelectedItem() != null && !(effect && effectRate <=0)) {
						tmp.addEvent(new LightEvent(((Channel) events.get(i).channel.getSelectedItem()), on, effect, effectRate));
					}
					else {
						System.err.println("Unable to add cue: Invalid Input.");
						feedback = new JLabel("Unable to add cue: Invalid Input.");
						feedback.setForeground(Color.red);
						cuePanel.add(feedback);
						JOptionPane.showMessageDialog(null, "Unable to add cue: Invalid Input.");

						success = false;
					}
				}
				if(success) { //See if sucessfull so far, then check if exists
					//Check if cue already exists
					for (int i=0; i<song.getCues().length; i++){
						Cue c = song.getCues()[i];
						//If runtime is less than current cue to check, end search
						if(tmp.getRunTime() < c.getRunTime()) break;
						else if (tmp.getRunTime() == c.getRunTime()) {
							//Cue already exists
							success = false;
							System.err.println("Cue already exists at that time");

							feedback = new JLabel("Unable to add cue: Cue already exists at that time.");
							feedback.setForeground(Color.red);
							cuePanel.add(feedback);
							JOptionPane.showMessageDialog(null, "Unable to add cue: Cue already exists at that time.");

						}
					}
				}
			}

			else {
				System.err.println("Unable to add cue: Invalid Cue Time.");
				success = false;

				feedback = new JLabel("Unable to add cue: Invalid Cue Time.");
				feedback.setForeground(Color.red);
				cuePanel.add(feedback);
				JOptionPane.showMessageDialog(null, "Unable to add cue: Invalid Cue Time.");
			}


			if(success) {
				//If input is valid, add cue and refresh
				song.addCue(tmp);
				System.out.println("Cue added.");
				return true;
			}
			else {
				System.err.println("Cue not added.");
				return false;
			}
		}
		//If ok wasn't selected, return true to confirm valid input
		return true;
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

	public boolean openFile(File file) {
		try {
			FileInputStream fin = new FileInputStream(file);
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

	public void resetTimer() {
		timer.reset();
	}		
}

class eventInput {
	JComboBox channel;
	JComboBox state;
	String[] options = {"On", "Off", "Effect"};
	JTextField rateInput;
	JButton rmvButton;
	private JPanel newChPanel;


	public eventInput(Song s) {
		newChPanel = new JPanel();

		channel = new JComboBox(s.getChannels());
		state = new JComboBox(options);
		rateInput = new JTextField();
		rateInput.setColumns(4);
		rmvButton = new JButton("Remove");


		//		newChPanel.setLayout(new BoxLayout(chPanel, BoxLayout.X_AXIS));
		//		int i = events.size()-1;

		//		newChPanel.add(Box.createHorizontalStrut(15)); // a spacer		

	}

	public int getEffectRate() {
		if(rateInput.getText() != null) {
			try {
				int effectRate = Integer.parseInt(rateInput.getText());
				if (effectRate > 0) {
					return effectRate;
				}
				else {
					System.err.println("Invalid effect input, rate must be greater than 0");
					return -1;
				}
			}
			catch (Exception e) {
				System.err.println("Exception: Invalid effect input");
				return -1;
			}
		}
		else {
			System.err.println("No rate provided");
			return -1;
		}
	}

	JPanel createChPanel() {
		newChPanel.add(new JLabel("Channel:"));
		newChPanel.add(channel);
		newChPanel.add(new JLabel("State:"));
		newChPanel.add(state);
		newChPanel.add(new JLabel("Eff. Rate:"));
		newChPanel.add(rateInput);

		//Remove Button
		newChPanel.add(rmvButton);

		return newChPanel;
	}

	JPanel getChPanel() {
		return newChPanel;
	}

}