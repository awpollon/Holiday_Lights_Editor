import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Song {

	private String title;
	private CueList cues;
	private Channel[] channels;	



	public Song(String songTitle, double songLength) {
		this.title = songTitle;
		cues = new CueList(songLength);
	}

	boolean addCue(Cue c) {
		return cues.addCue(c);
	}

	boolean removeCue(Cue c) {
		return false;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public Channel[] getChannels() {
		return channels;
	}

//	public void setChannels(Channel[] channels) {
//		this.channels = channels;
//	}
	

	public ArrayList<Cue> getCues() {
		return cues;
	}
	
}

