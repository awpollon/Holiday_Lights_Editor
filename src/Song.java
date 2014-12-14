import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Song {

	private String title;
	private ArrayList<Cue> cues;
	private Show show;
	private Channel[] channels;	



	public Song(String songTitle, Show s) {
		this.title = songTitle;
		cues = new ArrayList<Cue>();
		this.show = s;
	}

	boolean addCue(Cue c) {
		return cues.add(c);
	}

	boolean removeCue(Cue c) {
		return cues.remove(c);
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

