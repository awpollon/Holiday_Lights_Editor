import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;


public class Timer implements Runnable {
	Clip clip;
	
	byte[] bytesBuffer;
	int bytesRead;
	AudioInputStream audioStream;
	SourceDataLine audioLine;
	AudioFormat format;
	int BUFFER_SIZE = 4096;
	String audioFilePath = "/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/audio/Christmas_Eve-Sarajevo.wav";
	File audioFile;
	DataLine.Info info;
	
	Editor editor;
	public Timer(Editor e) {
		this.editor = e;
		
		//Load song
		
		try {
		audioFile = new File(audioFilePath);
		 
		audioStream = AudioSystem.getAudioInputStream(audioFile);
		 
		format = audioStream.getFormat();
				 
		info = new DataLine.Info(SourceDataLine.class, format);
		 
		audioLine = (SourceDataLine) AudioSystem.getLine(info);
		
		audioLine.open(format);
				 
		bytesBuffer = new byte[BUFFER_SIZE];
		bytesRead = -1;
		
		
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	@Override
	public void run()  {
		long startTime = System.currentTimeMillis();
		long editorStart = editor.getEditorTime();
	
		//Start audio
		audioLine.start();

	
		editor.isPlaying = true;

		while(editor.isPlaying){
			//Get new time and update editor
			long currentTime = System.currentTimeMillis(); 
			editor.setEditorTime((currentTime-startTime) + editorStart);
			editor.gui.updateTime();
			
			//Play sound
			try {
				if ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
				    audioLine.write(bytesBuffer, 0, bytesRead);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
		 
		audioLine.stop();
		 
	}
	
	void reset() {
		try {
			audioStream.close();
			audioLine.close();
			
			audioStream = AudioSystem.getAudioInputStream(audioFile);
			audioLine = (SourceDataLine) AudioSystem.getLine(info);

			audioLine.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		bytesBuffer = new byte[BUFFER_SIZE];
		bytesRead =-1;
	}

}
