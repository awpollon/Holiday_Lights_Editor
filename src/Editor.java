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
import javax.swing.filechooser.FileSystemView;

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

	public Editor(Song s) {		
		this.song = s;
		gui = new GUI(this);
		setEditorTime(0);
	}

	void stopTimer() {
		isPlaying = false;
		System.out.println("Stopped time: " + editorTime);
	}

	void startTimer() {
		(new Thread(new Timer(this))).start();
	}


	void refresh() {
		//Get list of cues
		this.song.getCues();
	}

	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
	    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Holiday LX Editor");

		
		
		Song s = new Song("Second Song");

		s.addChannel(new Channel("White Tree", 1, 2));
		s.addChannel(new Channel("Blue Tree", 2, 3));
		s.addChannel(new Channel("Blues", 3, 4));
		s.addChannel(new Channel("Whites", 4, 5));
		s.addChannel(new Channel("Wreaths", 5, 6));

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
			//Intro comment
			bw.append("/*\n");
			bw.append("This code has been generated by Song_Creater.java and is intended for an Arduino. \n Created by Aaron Pollon\n");
			bw.append("*/\n\n");
			//Setup method
			bw.append("void setup() {\n");
			Object[] chs = song.getChannels();

			try {
				for(int i=0; i<chs.length; i++) {
					if(chs[i] != null){
						Channel c = (Channel) chs[i];
						bw.append("pinMode(" + c.getArduinoPin() +", OUTPUT);\n");
					}
				}
			}
			catch(Exception e) {
				System.err.println("Error: Unable to print cues");
				bw.close();
				return false;
			}
			bw.append("}\n\n");

			//Loop
			bw.append("void loop() {\n");

			Object[] qs = song.getCues();

			for(int i=0; i<qs.length; i++) {
				Cue c = (Cue) qs[i];
				for(int j=0; j<c.getEvents().size(); j++) {
					LightEvent e = c.getEvents().get(j);
					bw.append("digitalWrite(" +e.channel.getChNum() +", ");
					if(e.on){
						bw.append("HIGH);");
					}
					else {
						bw.append("LOW);");
					}
					//Go to next line
					bw.append("\n");
				}
				//After each cue, place a delay equal to difference in timing
				//Check if there is another cue
				if(i<qs.length-1) {
					double delayTime = ((Cue) qs[i+1]).getRunTime() - c.getRunTime();
					bw.append("delay(" + delayTime +");\n");
				}
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
		final JPanel chPanel = new JPanel();
		final JScrollPane scp = new JScrollPane(cuePanel);
		scp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		chPanel.setLayout(new BoxLayout(chPanel, BoxLayout.X_AXIS));
		cuePanel.add(chPanel);		
		cueTime.setText("" + editorTime);
		//			JTextField channel = new JTextField(5);

		final ArrayList<eventInput> events = new ArrayList<eventInput>();

		//add initial event
		events.add(new eventInput(song));

		chPanel.add(new JLabel("Cue Time:"));
		chPanel.add(cueTime);
		chPanel.add(Box.createHorizontalStrut(15)); // a spacer

		chPanel.add(new JLabel("Channel:"));
		chPanel.add(events.get(0).channel);
		chPanel.add(Box.createHorizontalStrut(15)); // a spacer
		chPanel.add(new JLabel("State:"));
		chPanel.add(events.get(0).state);
		


		JButton addEvent = new JButton("Add Channels");
		
		chPanel.add(addEvent);

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
					boolean on = false;
					if(events.get(i).state.getSelectedItem().equals("On")) on = true;

					if(events.get(i).channel.getSelectedItem() != null) {
						tmp.addEvent(new LightEvent(((Channel) events.get(i).channel.getSelectedItem()), on));
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
				if(success) { //See if sucessfull so far, thn check if exists
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

	public void createNewFile(String fileName) {
		Song newSong = new Song(fileName);
		newSong.copySong(this.song);
		Editor newEditor = new Editor(newSong);
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
}

class eventInput {
	JComboBox channel;
	JComboBox state;
	String[] options = {"On", "Off"};
	JButton rmvButton;
	private JPanel newChPanel;

	
	public eventInput(Song s) {
		newChPanel = new JPanel();
		
		channel = new JComboBox(s.getChannels());
		state = new JComboBox(options);
		rmvButton = new JButton("Remove");

		
//		newChPanel.setLayout(new BoxLayout(chPanel, BoxLayout.X_AXIS));
//		int i = events.size()-1;

//		newChPanel.add(Box.createHorizontalStrut(15)); // a spacer		
		
	}
	
	JPanel createChPanel() {
		newChPanel.add(new JLabel("Channel:"));
		newChPanel.add(channel);
//		newChPanel.add(Box.createHorizontalStrut(15)); // a spacer
		newChPanel.add(new JLabel("State:"));
		newChPanel.add(state);
		
		//Remove Button
		newChPanel.add(rmvButton);
		
		return newChPanel;
	}
	
	JPanel getChPanel() {
		return newChPanel;
	}
	
}