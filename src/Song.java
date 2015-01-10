import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class Song implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4942039484548991991L;
	
	private String title;
	private ArrayList<Cue> cues;
	private ArrayList<Channel> channels;
	private String fileName;
//	private String audioFilePath;
	private File audioFile;

	private String fileLocation;	

	public Song(String songTitle, File audio) {
		this.title = songTitle;
		this.fileName = this.title + ".ser"; //Hardcode as .ser file for now
		cues = new ArrayList<Cue>();
		channels = new ArrayList<Channel>();
		fileLocation = "/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/savedFiles/";//hardcode for local file
//		this.audioFilePath = audioFilePath;
		this.audioFile = audio;
	}

	boolean addCue(Cue c) {
		 if (cues.add(c)) {
			 return true;
		 }
		 else return false;
	}

	boolean removeCue(Cue c) {
		return cues.remove(c);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.fileName = title + ".ser"; //Hard code .ser
	}
	public Channel[] getChannels() {
		return channels.toArray(new Channel[channels.size()]);
	}

	public boolean addChannel(Channel ch) {
		return this.channels.add(ch);
	}
	
	public boolean removeChannel(Channel ch) {
		return this.channels.remove(ch);
	}

	public Cue[] getCues() {

		Collections.sort(this.cues);
		return cues.toArray(new Cue[this.cues.size()]);
	}
	
	public ArrayList<Cue> getCueList() {
		Collections.sort(this.cues);
		return cues;
	}

	public void copySong(Song song) {
		this.channels = song.channels;
		this.cues = song.cues;
	}

	public String getFilePath() {
		return ("" + this.fileLocation + this.fileName);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public File getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(File newAudioFile) {
		this.audioFile = newAudioFile;
		//Will need to reload instantiate timer
	}
}

