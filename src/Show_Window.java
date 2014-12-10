import javax.swing.JFrame;


public class Show_Window {
	public Show_Window() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);


	}
	public static void main(String[] args) {
		Show_Window s = new Show_Window();
	}
}
