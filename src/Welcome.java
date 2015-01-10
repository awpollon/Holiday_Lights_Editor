import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class Welcome {	
	private JFrame frame;
	private JPanel panel;
	private JTextArea text;
	private JPanel buttons;
	private JButton newSong;
	private JButton openSong;
	
	private Song song;
	
	public Welcome() {		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setBounds(50, 100, 400, 200);
		frame.setVisible(true);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		frame.add(panel);
		text = new JTextArea("Welcome to the the app!");
		text.setEditable(false);
		panel.add(text);
		
		newSong = new JButton("Create New Song");
		newSong.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createNewSong();
			}
		});
		
		openSong = new JButton("Open Existing Song");
		openSong.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openSong();
			}
		});
		
		buttons = new JPanel();
		buttons.add(newSong);
		buttons.add(openSong);
		panel.add(buttons);
		
		frame.validate();
	}

	void openSong() {
		// TODO Auto-generated method stub
		
	}

	 void createNewSong() {
		song = new Song("Let It Go", "/Users/AaronPollon/Documents/Projects/Arduino_Song_Generator/audio/Let_It_Go.wav");

	}

	public Song getSong() {
		return song;
	}
	
}
