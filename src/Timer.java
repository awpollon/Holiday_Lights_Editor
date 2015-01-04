import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class Timer implements Runnable {
	Editor editor;
	public Timer(Editor e) {
		this.editor = e;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		long editorStart = editor.getEditorTime();

//		// open the sound file as a Java input stream
//		String sound = "/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/WWTheme.wav";
////		String sound = "/Users/AaronPollon/Desktop/play.mp3";
//		
//		InputStream in;
//		        	
//		try {
//			in = new FileInputStream(new File(sound));
//
//
//			// create an audiostream from the inputstream
//			AudioStream audioStream;
//
//			audioStream = new AudioStream(in);
//			// play the audio clip with the audioplayer class
//			AudioPlayer.player.start(audioStream);
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}



		editor.isPlaying = true;

		while(editor.isPlaying){
			long currentTime = System.currentTimeMillis(); 
			editor.setEditorTime((currentTime-startTime) + editorStart);
			editor.gui.updateTime();

			if((currentTime - startTime)%2000 == 0) {
				//				System.out.println(editor.getEditorTime());

			}

		}		
	}

}
