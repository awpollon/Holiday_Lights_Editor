import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class NewCueWindow {
	private JPanel p;
	private JFrame f;
	
	private final JLabel timeLabel = new JLabel("Time: ");
	private JTextField time;
	private final JLabel chLabel = new JLabel("Channel: ");
	private JTextField ch;
	private JButton add;
	private JButton cancel;
	
	
	double cueTime;
	int cueCh;
	boolean wasCancelled = false;
	
	
	public NewCueWindow() {
		String x = JOptionPane.showInputDialog(null);
		
		
		f = new JFrame("New Cue");
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.setBounds(50, 100, 200, 200);
		f.setVisible(true);

		p = new JPanel();
		p.setLayout(new FlowLayout());
		f.add(p);
		
		
		time = new JTextField();
		time.setColumns(3);
		ch = new JTextField();
		ch.setColumns(3);
		
		add = new JButton("Add Cue");
		cancel = new JButton("Cancel");
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wasCancelled = true;
			}
		});
		
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wasCancelled = false;
				validateInput();
			}
		});
		
		p.add(timeLabel);
		p.add(time);
		p.add(chLabel);
		p.add(ch);
		p.add(add);
		p.add(cancel);
		
		f.validate();
	}


	 void validateInput() {
		cueTime = Double.parseDouble(time.getText());
		cueCh = Integer.parseInt(ch.getText());
		
		if (cueTime >= 0 || cueCh >= 0) {
			f.setVisible(false);
		}
		else {
			System.out.println("Invalid input");
		}
	}
	
}
