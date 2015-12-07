

public class EffectsTimer extends KeyTimer {
	private Editor editor;

	public EffectsTimer(Editor theEditor) {
		super();
		this.editor = theEditor;
	}
	
	@Override
	void start() {
		double songTime = editor.getEditorTime() / 1000.00;
		
		if(!editor.isPlaying()) editor.startTimer();
		super.start();
		this.outputLine("Start time: " + songTime);
	}
	@Override
	void stop() {
		if(editor.isPlaying()) editor.stopTimer();
		super.stop();
	}

}
