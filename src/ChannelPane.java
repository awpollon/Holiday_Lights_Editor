import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.plaf.ColorChooserUI;


public class ChannelPane {
	Editor editor;
	
	JPanel chPanel;
	JPanel channelPanels;
	JScrollPane scp;
	Channel[] chs;
	
	public ChannelPane(Editor e) {
		this.editor = e;
		chs = e.getCurrentSong().getChannels();
		
		chPanel = new JPanel();
		chPanel.setLayout(new BoxLayout(chPanel, BoxLayout.Y_AXIS));
		
		JPanel chHeading = new JPanel();
		chHeading.setLayout(new GridLayout());
		chHeading.add(new JLabel("Num"));
		chHeading.add(new JLabel("Name"));
		chHeading.add(new JLabel("Color"));
		chHeading.add(new JLabel("Arduino Pin"));
		chHeading.add(new JLabel("Remove"));

		
		channelPanels = new JPanel();
		channelPanels.setLayout(new BoxLayout(channelPanels, BoxLayout.Y_AXIS));
				
		scp = new JScrollPane(channelPanels);
		scp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Add channel button
		JButton addChButton = new JButton("Add Channel");
		addChButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addChannel(new Channel("", 0, 0, null));
				scp.validate();
			}
		});
		
		chPanel.add(chHeading);
		chPanel.add(scp);
		chPanel.add(addChButton);
		
		
		//Create new panel for each channel
		for(Channel c: chs) {
			addChannel(c);
		}
		
		chPanel.validate();
		int result = JOptionPane.showConfirmDialog(null, chPanel, 
				"Edit Channels", JOptionPane.OK_CANCEL_OPTION);

	}
  
	private void addChannel(Channel c) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());

		panel.add(new JLabel(c.getChNum() + ""));
		panel.add(new JTextField(c.getChName()));
		panel.add(new JComboBox());
		panel.add(new JTextField(c.getArduinoPin() + ""));	
		JButton remove = new JButton("-");
		remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Will remove...does nothing yet.");
			}
		});
		
		panel.add(remove);
		
		channelPanels.add(panel);
	}
}
