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

	public Song(String songTitle, double songLength) {
		this.title = songTitle;
		cues = new ArrayList<Cue>();
		channels = new ArrayList<Channel>();
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
	}
	public Object[] getChannels() {
		return channels.toArray();
	}

	public boolean addChannel(Channel ch) {
		return this.channels.add(ch);
	}
	
	public boolean removeChannel(Channel ch) {
		return this.channels.remove(ch);
	}

	public Object[] getCues() {
		Collections.sort(this.cues);
		return (cues.toArray());
	}
	
}

