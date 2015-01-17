import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.swing.SwingUtilities;


public class Timer implements Runnable {
	private byte[] bytesBuffer;
	private int bytesRead;
	private AudioInputStream audioStream;
	SourceDataLine audioLine; //public so editor can access due to threading issue w/ stopping
	private AudioFormat format;
	private int BUFFER_SIZE = 4096;
	private File audioFile;
	private DataLine.Info info;
	
	private double songLengthInMillis;
	public double resetOffsetMillis;

	Editor editor;
	public Timer(Editor e) {
		this.editor = e;
		
		//Load song
		if(loadAudioFile()) {
			//set editor time when loaded
			updateEditorTime();
		}
		else {
			System.err.println("Unable to load file");
		}

	}

	@Override
	public void run()  {

		//Start audio
		audioLine.start();
		editor.setIsPlaying(true);

		while(editor.isPlaying()){

			if(playNextSoundByte()) {
				updateEditorTime();
			}
			else {
				System.err.println("Error: Unable to play sound byte");
			}
		}	

		audioLine.stop();

	}

	public void updateEditorTime() {
		//Update timer
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				//get current position plus the offset from reset.
				editor.setEditorTime((audioLine.getMicrosecondPosition() / 1000) + resetOffsetMillis); //Convert to millis							
			}
		});		
	}

	private boolean playNextSoundByte() {
		try {
			if ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
				audioLine.write(bytesBuffer, 0, bytesRead);
			}
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	void reset(double percent) {
		try {
			audioStream.close();
			audioLine.close();

			audioStream = AudioSystem.getAudioInputStream(audioFile);
			audioLine = (SourceDataLine) AudioSystem.getLine(info);

			setAudioPlace(percent);
			audioLine.open();

			resetOffsetMillis = percent * songLengthInMillis;
					
			//set editor time at start
			updateEditorTime();

		} catch (Exception e) {
			e.printStackTrace();
		}

		bytesBuffer = new byte[BUFFER_SIZE];
		bytesRead =-1;
	}

	public void stopAudio(){
		//		audioLine.stop(); //Called by editor due threading issue
		//		editor.setIsPlaying(false);
		//		updateEditorTime();
		System.out.println("Stopped time: " + editor.getEditorTime());

	}

	private boolean loadAudioFile() {

		try {
			audioFile = editor.getCurrentSong().getAudioFile();

			audioStream = AudioSystem.getAudioInputStream(audioFile);

			format = audioStream.getFormat();

			info = new DataLine.Info(SourceDataLine.class, format);

			audioLine = (SourceDataLine) AudioSystem.getLine(info);

			audioLine.open(format);

			bytesBuffer = new byte[BUFFER_SIZE];
			bytesRead = -1;

			songLengthInMillis = audioStream.getFrameLength() / 44.10; //44.100 frames/millis standard
			resetOffsetMillis = 0;
			
			return true;			
		}

		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean setAudioPlace(double percent) {
		if(percent >=0 && percent <1){	
			long targetBytes = (long) (percent * audioFile.length());

			if (setNextSoundByte(targetBytes)) {
				System.out.println("Skipping to " + targetBytes);
				return true;
			}
			else {
				System.err.println("Unable to skip in song");
				return false;
			}
		}
		else {
			System.err.println("Error: Invalid percent. Must be between 0 and 1");
			return false;
		}

	}

	private synchronized boolean setNextSoundByte(long numBytes) {
		try {
			audioStream.skip(numBytes);
			
//			Control[] controls = audioLine.getControls();
//			FloatControl sampleRate = (FloatControl) audioLine.getControl(controls[3].getType());
//			audioLine.getLineInfo();
//			sampleRate.setValue(2000);
//			System.out.println(" " + audioLine.getLongFramePosition());
//			System.out.println(audioStream.getFrameLength());
//			long songLength = audioStream.getFrameLength() / 44100;
//			System.out.println("Song lenght in secs: " + songLength);
//			audioLine.getMicrosecondPosition();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
