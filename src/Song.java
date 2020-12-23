import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Song implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4942039484548991991L;

	private String title;
	private ArrayList<Cue> cues;
	private ArrayList<Channel> channels; //TODO: remove

	private HashMap<Integer, Channel> channelsMap;
	private String fileName;
	//	private String audioFilePath;
	private File audioFile;

	private String fileLocation;	
	
//	private double lagMod = .85; //Modifier to account for delay with music shield

	public Song() {
	}

	public Song(String songTitle, File audio, LightsEditorApplication app) {
		this.title = songTitle;
		this.fileName = this.title + ".ser"; //Hardcode as .ser file for now
		cues = new ArrayList<Cue>();
//		channels = new ArrayList<Channel>();
		this.channelsMap = new HashMap<>();
		fileLocation = app.getSavedFilePath();
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

	public HashMap<Integer, Channel> getChannelsMap() {
		return channelsMap;
	}

	public Channel getChannel(Integer number) {
		return this.channelsMap.get(number);
	}

	public void addChannel(Channel ch) {
		this.channelsMap.put(ch.getChNum(), ch);
	}

	public void removeChannel(Channel ch) {
		this.channelsMap.remove(ch.getChNum());
	}

	public Cue[] getCues() {

		Collections.sort(this.cues);
		return cues.toArray(new Cue[this.cues.size()]);
	}

	@JsonIgnore
	public ArrayList<Cue> getCueList() {
		Collections.sort(this.cues);
		return cues;
	}

	public void copySong(Song song) {
		this.channelsMap = song.channelsMap;
		this.cues = song.cues;
	}

	@JsonIgnore
	public String getFilePath() {
		//Return location plus fileName, replacing spaces with underscores
		return ("" + this.fileLocation + "/" + this.fileName.replace(' ', '_'));
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

	public void setChStates(Cue c) {
		//Will set the state of each channel based on cue provided
		this.channelsMap.values().forEach(ch -> {
			boolean found = false;

			//Go backwards through each cue, starting with current
			for(int i=cues.indexOf(c); i>=0; i--){
				for(LightEvent ev: cues.get(i).getEvents()) {
					if(ev.getChannelNum() == ch.getChNum()) {
						found = true;
						ch.setCurrentState(ev.getState(), cues.get(i), ev.getEffectRate());
//						System.out.println("State of " + ch.getChName() + " in cue " + c + ": " + ch.getCurrentState() +", set in cue " + cues.get(i).getRuntTimeInSecs()); 
						break;
					}
					//If last state changed has been found, stop looking
					if(found) break;
				}
				if(found) break;
			}
		});
	}

	public static void checkFile(Song song) {
		//Checks the current file for missing data


		{ //Check for missing events state values
			boolean errFound = false;
			for(Cue q: song.getCueList()) {
				for(LightEvent ev: q.getEvents()) {
					if(ev.getState() == 0) {
						System.err.println("Cue " + q.getRuntTimeInSecs() + ", event " + ev.getChannelNum() + ": Missing state");
						errFound = true;
					}
				}
			}
			if(errFound) {
				int fix = JOptionPane.showConfirmDialog(null, "Cue events missing state, fix?", "Error Found", JOptionPane.OK_CANCEL_OPTION);
				if(fix == JOptionPane.OK_OPTION) {
					for(Cue q: song.getCueList()) {
						for(LightEvent ev: q.getEvents()) {
							if(ev.getState() == 0) {
								ev.setState();
							}
						}
					}
				}
			}

		}


//		//check for colors
//		{
//			for(Channel ch: song.getChannelsMap().values()) {
//				if(ch.getColor() == null) {
//					System.err.println("Error: Ch. " + ch.toString() + " missing color");
//					String input = JOptionPane.showInputDialog("type color name");
//					Field f;
//					try {
//						f = Color.class.getField(input);
//						System.out.println(f.get(null));
//						ch.setColor((Color) f.get(null));
//
//					} catch (Exception e) {
//						System.err.println("Error: unable to set color");
//					}
//				}
//
//			}
//		}
	}

	@JsonIgnore
	public double getLagMod() {
		return 1.0;
	}

	@JsonIgnore
	public boolean getPlayMusic() {
		return false;
	}

	@JsonIgnore
	public String getSDSongName() {
		return "track_4.mp3";
	}

	@JsonIgnore
	//Lower the louder
	public int getVolume() {
		return 40;
	}

	public void migrateLegacy() {
		if (this.channels != null) {
			this.channelsMap = new HashMap<>();
			for (Channel channel: this.channels) {
				this.channelsMap.put(channel.getChNum(), channel);
			}
			this.channels = null;
		}
		this.channelsMap.values().forEach(Channel::migrateLegacy);
		this.cues.forEach(Cue::migrateLegacy);
	}
}

