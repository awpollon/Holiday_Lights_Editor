import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
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
				editor.setEditorTime((audioLine.getMicrosecondPosition() / 1000)); //Convert to millis							
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

	void reset() {
		try {
			audioStream.close();
			audioLine.close();

			audioStream = AudioSystem.getAudioInputStream(audioFile);
			audioLine = (SourceDataLine) AudioSystem.getLine(info);

			audioLine.open();


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

			return true;			
		}

		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
