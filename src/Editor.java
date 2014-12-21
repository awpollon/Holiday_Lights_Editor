import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JList;



public class Editor implements Serializable{
	private Song currentSong;
	private long editorTime;
	boolean isPlaying;
	GUI gui;

	public Editor(Song s) {
		this.currentSong = s;
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
		this.currentSong.getCues();
	}

	public static void main(String[] args) {
		Song s = new Song("First Song", 60000);

		Editor e = new Editor(s);

	}
	
	
	public boolean writeFile(Song s){
		try {

			File file = new File(s.getTitle());
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			file = new File(s.getTitle() +"/"+s.getTitle()+".txt");

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
			Channel chs[] = s.getChannels();

			for(int i=0; i<chs.length; i++) {
				if(chs[i] != null){
					Channel c = chs[i];
					bw.append("pinMode(" + c.getArduinoPin() +", OUTPUT);\n");
				}
			}
			bw.append("}\n\n");

			//Loop
			bw.append("void loop() {\n");

			ArrayList<Cue> qs = s.getCues();

			for(int i=0; i<qs.size(); i++) {
				Cue c = qs.get(i);
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
				if(i<qs.size()-1) {
					double delayTime = qs.get(i+1).getRunTime() - c.getRunTime();
					bw.append("delay(" + delayTime +");\n");
				}
			}

			//End loop
			bw.append("}");


			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Song getCurrentSong() {
		return currentSong;
	}

	public long getEditorTime() {
		return editorTime;
	}

	public void setEditorTime(long editorTime) {
		this.editorTime = editorTime;
	}


	public void addNewCue() {
		//Handle button click to add new cue
		NewCueWindow n = new NewCueWindow();
	}

}
