import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class EventInput {
	public static final String onText = "On";
	public static final String offText = "Off";
	public static final String effectText = "Effect";

	
	private JComboBox channel;
	private JComboBox state;
	private String[] options = {onText, offText, effectText};
	private JTextField rateInput;
	private JButton rmvButton;
	private JPanel newChPanel;
	
	private CuePane cuePane;


	public EventInput(Song s, CuePane qPane) {
		this.cuePane = qPane;
		
		newChPanel = new JPanel();

		channel = new JComboBox(s.getChannels());
		state = new JComboBox(options);
		rateInput = new JTextField();
		rateInput.setColumns(4);
		rmvButton = new JButton("Remove");
		
		rmvButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeEvent();				
			}
		});


		//		newChPanel.setLayout(new BoxLayout(chPanel, BoxLayout.X_AXIS));
		//		int i = events.size()-1;

		//		newChPanel.add(Box.createHorizontalStrut(15)); // a spacer		

	}

	protected void removeEvent() {
		cuePane.removeEvent(this);	
	}

	public int getEffectRate() {
		if(rateInput.getText() != null) {
			try {
				double effectRateinSecs = Double.parseDouble(rateInput.getText());
				if (effectRateinSecs > 0) {
					
					
					return (int) (Math.floor(effectRateinSecs * 1000));
				}
				else {
					System.err.println("Invalid effect input, rate must be greater than 0");
					return -1;
				}
			}
			catch (Exception e) {
				System.err.println("Exception: Invalid effect input");
				return -1;
			}
		}
		else {
			System.err.println("No rate provided");
			return -1;
		}
	}

	JPanel createChPanel() {
		newChPanel.add(new JLabel("Channel:"));
		newChPanel.add(channel);
		newChPanel.add(new JLabel("State:"));
		newChPanel.add(state);
		newChPanel.add(new JLabel("Eff. Rate:"));
		newChPanel.add(rateInput);

		//Remove Button
		newChPanel.add(rmvButton);

		return newChPanel;
	}

	JPanel getChPanel() {
		return newChPanel;
	}

	public Channel getChannel() {
		return (Channel) channel.getSelectedItem();
	}

	public void setChannel(Channel ch) {
		this.channel.setSelectedItem(ch);
	}

	public boolean getStateisEffect() {
		return (state.getSelectedItem() == effectText);
	}
	
	public boolean getStateisOn() {
		return (state.getSelectedItem() == onText);
	}

	public void setState(LightEvent le) {
		if(le.isEffect()) {
			this.state.setSelectedItem(effectText);

		}
		else if(le.isOn()) {
			this.state.setSelectedItem(onText);

		}
		else {
			this.state.setSelectedItem(offText);

		}
	}

	public String getRateInput() {
		return rateInput.getText();
	}

	public void setRateInput(double rate) {
		rateInput.setText(rate + "");
	}

//	public JButton getRmvButton() {
//		return rmvButton;
//	}

//	public void setRmvButton(JButton rmvButton) {
//		this.rmvButton = rmvButton;
//	}

}
