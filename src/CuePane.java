import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class CuePane {	
	//Elements
	JTextField cueTime = new JTextField(5);
	JLabel feedback; //Only initilized if error needs to be given to user
	final JPanel cuePanel;
	final JScrollPane scp;

	//Data
	final ArrayList<EventInput> events = new ArrayList<EventInput>();
	Editor editor = null;
	Cue newCue;
	String titleText;


	public CuePane(final Editor ed, Cue cue, String title) {
		this.editor = ed;
		this.titleText = title;

		cuePanel = new JPanel();
		cuePanel.setLayout(new BoxLayout(cuePanel, BoxLayout.Y_AXIS));

		//		final JPanel chPanel = new JPanel();
		//May not need
		scp = new JScrollPane(cuePanel);
		scp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//Delete?
		//		chPanel.setLayout(new BoxLayout(chPanel, BoxLayout.X_AXIS));
		//		cuePanel.add(chPanel);		



		cueTime.setText("" + cue.getRuntTimeInSecs());
		
		//Create first panel
		JPanel firstPanel = new JPanel();
		firstPanel.add(new JLabel("Cue Time:"));
		firstPanel.add(cueTime);
		cuePanel.add(firstPanel);

		JButton addEvent = new JButton("Add Channels");

		firstPanel.add(addEvent);

		addEvent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addEventInput();

			}
		});

		//Go through each event and set values

		for(LightEvent e: cue.getEvents()) {
			EventInput eIn = new EventInput(editor.getCurrentSong(), this);
			eIn.setChannel(e.getChannel());
			eIn.setState(e);
			eIn.setOnRateInput(e.getEffectOnRate()/1000.00);
			eIn.setOffRateInput(e.getEffectOffRate() / 1000.00);

			events.add(eIn);
			cuePanel.add(eIn.createChPanel());
		}

		//		//add initial event
		//		EventInput firstEvent = new EventInput(editor.getCurrentSong());
		//		events.add(firstEvent);
		//		cuePanel.add(firstEvent.createChPanel());

		while(!getInput());
	}

	private void addEventInput() {
		final EventInput event = new EventInput(editor.getCurrentSong(), this);
		
		events.add(event);

		cuePanel.add(event.createChPanel());
		scp.validate();	
	}

	private boolean getInput(){
		int result = JOptionPane.showConfirmDialog(null, scp, 
				titleText, JOptionPane.OK_CANCEL_OPTION);


		if (result == JOptionPane.OK_OPTION) {

			//Validate input
			boolean success = true;
			double qTime = -1;
			Cue tmp = null;
			try{
				qTime = Double.parseDouble(cueTime.getText());


				tmp = new Cue(qTime*1000); //Convert to millis

			}
			catch(Exception e) {
				success = false;
				System.err.println("Cannot parseDouble time");

			}

			if(qTime >=0 && success) {
				assert tmp != null;
				for(int i=0; i<events.size(); i++) {
					boolean on = false;
					boolean effect = false;					
					EventInput ei = events.get(i);
//					int effectRate = 0;
					long effectOnRate = 0;
					long effectOffRate = 0;

					//Check if effect
					if(ei.getStateisEffect()) {
						effect = true;
						effectOnRate = ei.getEffectOnRate();
						effectOffRate= ei.getEffectOffRate();
					}

					if(ei.getStateisOn()) on = true;

					Channel getCh = events.get(i).getChannel();

					if(getCh != null && !(effect && effectOnRate <=0 )) {
						tmp.addEvent(new LightEvent(getCh, on, effect, effectOnRate, effectOffRate));
					}
					else {
						System.err.println("Unable to add cue: Invalid Input.");
						feedback = new JLabel("Unable to add cue: Invalid Input.");
						feedback.setForeground(Color.red);
						cuePanel.add(feedback);
						JOptionPane.showMessageDialog(null, "Unable to add cue: Invalid Input.");

						success = false;
					}
				}
				//				if(success) { //See if sucessfull so far, then check if exists
				//					//Check if cue already exists
				//					for (int i=0; i<editor.getCurrentSong().getCues().length; i++){
				//						Cue c = editor.getCurrentSong().getCues()[i];
				//						//If runtime is less than current cue to check, end search
				//						if(tmp.getRunTime() < c.getRunTime()) break;
				//						else if (tmp.getRunTime() == c.getRunTime()) {
				//							//Cue already exists
				//							success = false;
				//							System.err.println("Cue already exists at that time");
				//
				//							feedback = new JLabel("Unable to add cue: Cue already exists at that time.");
				//							feedback.setForeground(Color.red);
				//							cuePanel.add(feedback);
				//							JOptionPane.showMessageDialog(null, "Unable to add cue: Cue already exists at that time.");
				//
				//						}
				//					}
				//				}
			}

			else {
				System.err.println("Unable to add cue: Invalid Cue Time.");
				success = false;

				feedback = new JLabel("Unable to add cue: Invalid Cue Time.");
				feedback.setForeground(Color.red);
				cuePanel.add(feedback);
				JOptionPane.showMessageDialog(null, "Unable to add cue: Invalid Cue Time.");
			}


			if(success) {
				//If input is valid, set newCue
				newCue = tmp;
				System.out.println("newCue set");
				return true;
			}
			else {
				System.err.println("Cue not added.");
				return false;
			}
		}
		//If ok wasn't selected, return true to confirm valid input
		return true;
	}

	public Cue getCue() {
		return this.newCue;
	}

	public void removeEvent(EventInput event) {
		cuePanel.remove(event.getChPanel());
		events.remove(event);
		scp.validate();
	}

}
