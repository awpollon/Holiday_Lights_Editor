import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUI implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2843788118490686677L;

	Editor e;
	
	JTextField timeText;
	JPanel p;
	JFrame f;
	
	
	JList list;
	
	JButton start;
	JButton addCue;

	private Object[] cues;
	

	public GUI(Editor editor) {
		this.e = editor; //Specifies the editor session
		
		f = new JFrame("Show Editor");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(50, 100, 300, 300);
		f.setVisible(true);

		p = new JPanel();
		p.setLayout(new BorderLayout());
		f.add(p);

		timeText = new JTextField("Time: " + e.getEditorTime());
		timeText.setEditable(false);
		p.add(timeText, BorderLayout.PAGE_START);
		
		//Test: print cue list
		cues= e.getCurrentSong().getCues();

		
		list = new JList(e.getCurrentSong().getCues());
		p.add(list,BorderLayout.CENTER);
		
		
		start = new JButton("Play");
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleButtonClick((JButton)e.getSource());
			}
		});
		p.add(start, BorderLayout.SOUTH);

		addCue = new JButton("Add Cue");
		addCue.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.addNewCue();
			}
		});
		
		p.add(addCue, BorderLayout.EAST);
		
		f.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println(e.getKeyChar()+ " pressed!");
			}

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(e.getKeyChar()+ " released!");

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}


		});

		f.validate();

	}
	protected void handleButtonClick(JButton b) {
		if(b.getText() == "Play") {
			e.startTimer();
			b.setText("Stop");
		}
		else if (b.getText() == "Stop") {
			e.stopTimer();
			b.setText("Play");
		}
	}
	
	void updateTime() {
		timeText.setText("Time: " + e.getEditorTime());		
	}
	
	void printCues() {
		this.cues= e.getCurrentSong().getCues();
		list = new JList(e.getCurrentSong().getCues());
		
		for (int i=0; i<cues.length; i++) {
			System.out.println(cues[i]);
		}
	}
}
