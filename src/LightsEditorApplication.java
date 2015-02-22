import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class LightsEditorApplication {
	public LightsEditorApplication() {
		setupApp();
		startApp();
	}
	
	public static void main(String[] args) {
		LightsEditorApplication app = new LightsEditorApplication();
	}

	boolean setupApp(){
	return true;
	}
	
	void startApp(){
	
		//Start memore check thread
		//		Thread mc = new Thread(new Memory_Check());
		//		mc.start();

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
				if(this.createNewSong()) success = true;
				else success = false;
			}
			else if(selection == 1) {
				//Open song
				if(!this.openFile()) {
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
		//
		//		Editor e = new Editor(s);

		//		e.gui.printCues();

	}
	
	
	private boolean createNewSong() {
		boolean validName = false;
		String songName = null;
		while(!validName) {
			songName = JOptionPane.showInputDialog("Enter Song Name");


			if (songName == null) return false;
			else if (songName.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a song title.");
				validName = false;
			}
			else validName = true;
		}
		File soundFile = promptAudioFile();

		if(soundFile != null){
			Song newSong = new Song(songName, soundFile);
			//			newSong.copySong(this.song); //Reference for SAVE AS implementation

			//Hardcode channels
			newSong.addChannel(new Channel("White Tree", 1, 8, Color.WHITE));
			newSong.addChannel(new Channel("Blue Tree", 2, 9, Color.blue));
			newSong.addChannel(new Channel("Blues", 3, 3, Color.blue));
			newSong.addChannel(new Channel("Whites", 4, 5, Color.white));
			newSong.addChannel(new Channel("Wreaths", 5, 2, Color.orange));

			//Start with a cue at 0.0 with everything off
			Cue firstCue = new Cue(0);
			firstCue.setActive(true);

			for(Channel c: newSong.getChannels()) {
				firstCue.addEvent(new LightEvent(c, false, false, 0));
			}
			newSong.addCue(firstCue);


			//Instantiate editor
			Editor newEditor = new Editor(newSong, this);
			//Save the file to start
			newEditor.saveFile();

			return true;
		}
		return false;
	}
	
	public File promptAudioFile() {

		JFileChooser fc = new JFileChooser("/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/audio");

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"WAV", "wav");
		fc.setFileFilter(filter);					
		int opened = fc.showDialog(null, "Open");
		if (opened == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		else return null;
	}
	
	public boolean openFile() {
		System.out.println("Opening");
		JFileChooser fc = new JFileChooser("/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/SavedFiles");


		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Ser", "ser");
		fc.setFileFilter(filter);					
		int opened = fc.showDialog(null, "Open");
		if (opened == JFileChooser.APPROVE_OPTION) {

			try {
				FileInputStream fin = new FileInputStream(fc.getSelectedFile());
				ObjectInputStream ois = new ObjectInputStream(fin);
				Song openSong = (Song) ois.readObject();
				Editor newEditor = new Editor(openSong, this);

				//Update file path in song file
				openSong.setFileLocation(fc.getSelectedFile().getParent());

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

}
