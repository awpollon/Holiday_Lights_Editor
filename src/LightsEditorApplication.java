import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class LightsEditorApplication {
	private String audioStartPath = null;
	private String savedFilePath = null;
	private String arduinoExportPath = null;
	private String appName = null;

	public LightsEditorApplication() {
		setupApp();
		startApp();
	}
	
	public static void main(String[] args) {
		LightsEditorApplication app = new LightsEditorApplication();
	}

	boolean readConfigFile() throws IOException{
		Properties prop = new Properties();
		String propFileName = "config.properties";
 
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
  
		// get the property value and print it out
		this.appName = prop.getProperty("appName");
		this.audioStartPath  = prop.getProperty("audioStartingFilePath");
		this.savedFilePath = prop.getProperty("savedFilePath");
		this.arduinoExportPath = prop.getProperty("arduinoExportPath");
		
		return true;
	}
	
	boolean setupApp(){
		try {
			this.readConfigFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
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
			Song newSong = new Song(songName, soundFile, this);
			//			newSong.copySong(this.song); //Reference for SAVE AS implementation


			//Hardcode channels for new song
			newSong.addChannel(new Channel("White Tree", 1, 47, Color.WHITE));
			newSong.addChannel(new Channel("Blue Tree", 2, 46, Color.blue));
			newSong.addChannel(new Channel("Blues", 3, 48, Color.blue));
			newSong.addChannel(new Channel("Whites", 4, 49, Color.white));
			newSong.addChannel(new Channel("Multi Top", 5, 44, Color.ORANGE));
			newSong.addChannel(new Channel("Wreaths", 6, 45, Color.orange));
			newSong.addChannel(new Channel("Red", 7, 43, Color.red));
			newSong.addChannel(new Channel("Multi Arch", 8, 42, Color.ORANGE));

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

		JFileChooser fc = new JFileChooser(audioStartPath);

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
		JFileChooser fc = new JFileChooser(savedFilePath );


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

	public String getArduinoExportPath() {
		return arduinoExportPath;
	}

	public String getAppName() {
		return appName ;
	}

	public String getSavedFilePath() {
		return savedFilePath;
	}

}
