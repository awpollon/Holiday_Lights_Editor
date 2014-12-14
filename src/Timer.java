
public class Timer implements Runnable {
	Editor editor;
	public Timer(Editor e) {
		this.editor = e;
	}
	
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		long editorStart = editor.editorTime;
		
		editor.isPlaying = true;

		while(editor.isPlaying){
			long currentTime = System.currentTimeMillis(); 
			editor.editorTime = (currentTime-startTime) + editorStart;
			editor.updateTime();
			
			if((currentTime - startTime)%2000 == 0) {
				System.out.println(editor.editorTime);

			}
			
		}		
	}

}
