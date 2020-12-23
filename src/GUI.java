import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class GUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2843788118490686677L;

	Editor e;

	JTextField timeText;
	JPanel p;
	JFrame f;

	JPanel buttonPanel;
	JPanel topButtonPanel;
	JPanel bottomButtonPanel;
	JPanel eventPanel;
	JScrollPane eventScrlPane;

	JPanel statePanel;

	JPanel leftPanel;
	JList list;

	JMenuBar menubar;
	JMenu file;
	JMenuItem save;
	JMenuItem saveAs;
	JMenuItem open;
	JMenu export;
	JMenuItem quit;

	JMenu edit;
	JMenuItem newAudio;
	JMenuItem renameSong;
	JMenuItem editChannels;
	JMenuItem duplicateCue;
	JMenuItem checkFile;

	JMenu help;
	JMenuItem info;


	JButton start;
	JButton reset;
	JButton addCue;
	JButton editCue;
	JButton removeCue;
	JButton openEffectsTimer;

	JCheckBox setLiveBox;
	JSlider audioSlider;

	private final int sliderMaxVal = 100;

	private Object[] cues;


	public GUI(final Editor editor) {
		this.e = editor; //Specifies the editor session

		f = new JFrame(Editor.appName + ": \"" + e.getCurrentSong().getTitle() +"\"");
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.setBounds(50, 100, 800, 400);
		f.setVisible(true);


		p = new JPanel();
		p.setLayout(new BorderLayout());
		f.add(p);

		//Set up menu bar
		menubar = new JMenuBar();
		file = new JMenu("File");
		save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int confirmSave = JOptionPane.showConfirmDialog(null, "Save " + e.getCurrentSong().getTitle() + "?", "Save", JOptionPane.YES_NO_OPTION);
				if (confirmSave == JOptionPane.YES_OPTION){
					System.out.println("Saving");
					e.saveFile();
				}
			}
		});

		saveAs = new JMenuItem("Save As");
		saveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String fileName = JOptionPane.showInputDialog("Enter file name");
				if(fileName != null){
					//					e.createNewFile(fileName, e.getCurrentSong().getAudioFilePath()); //Uses current audio file
				}
			}
		});

		open = new JMenuItem("Open File");
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				int confirmOpen = JOptionPane.showConfirmDialog(null, "Open New File?", "Open", JOptionPane.YES_NO_OPTION);
				if (confirmOpen == JOptionPane.YES_OPTION){
					e.openFile();
				}
			}
		});

		export = new JMenu("Export");
		JMenuItem toSketch = new JMenuItem("Arduino Sketch");
		JMenuItem toJson = new JMenuItem("JSON");

		export.add(toSketch);
		export.add(toJson);

		toSketch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Exporting to Arduino Sketch");
				if (e.writeFile()){
					System.out.println("Sketch saved");
				}
				else {
					System.err.println("Sketch not saved");
				}
			}
		});

		toJson.addActionListener(e1 -> {
			System.out.println("Exporting to JSON");
			e.exportToJson();
		});

		quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit", JOptionPane.OK_CANCEL_OPTION);
				if(confirm == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});

		file.add(save);
		file.add(saveAs);
		file.add(open);
		file.add(export);
		file.add(quit);

		edit = new JMenu("Edit");

		newAudio = new JMenuItem("Change Audio");
		newAudio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.changeAudio();	
			}
		});

		edit.add(newAudio);

		checkFile = new JMenuItem("Error Check File");
		checkFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Song.checkFile(editor.getCurrentSong());
			}
		});
		edit.add(checkFile);

		editChannels = new JMenuItem("Edit Channels");
		editChannels.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.showEditChannels();
			}
		});
		edit.add(editChannels);

		duplicateCue = new JMenuItem("Duplicate Cue");
		duplicateCue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		renameSong = new JMenuItem("Rename Song");
		renameSong.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String input = JOptionPane.showInputDialog("Enter new song name");
				e.getCurrentSong().setTitle(input);
			}
		});

		help = new JMenu("Help");
		info = new JMenuItem("About " + Editor.appName);
		info.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JOptionPane x = new JOptionPane();

			}
		});
		help.add(info);

		menubar.add(file);
		menubar.add(edit);
		menubar.add(help);


		f.setJMenuBar(menubar);
		//		p.add(menubar, BorderLayout.PAGE_START);

		timeText = new JTextField();
		updateTime();
		timeText.setEditable(false);
		timeText.setColumns(9);


		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));


		//Test: print cue list
		cues= e.getCurrentSong().getCues();


		list = new JList(e.getCurrentSong().getCues());

		list.setFixedCellWidth(60);

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setCellRenderer(new CueListRenderer());
		list.setVisibleRowCount(-1);

		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseClicked(MouseEvent me) {
				///If cue is double clicked, ope up edit dialog
				if (me.getClickCount() >= 2) {
					e.editCue((Cue) list.getSelectedValue());
				}
			}
		});

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				//Check it is done changing and there is a value selected
				if(!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
					//Enable remove and edit cue buttons
					removeCue.setEnabled(true);
					editCue.setEnabled(true);

					Cue selected = (Cue) list.getSelectedValue();
					GUI.this.e.setSelectedCue(selected);

					//Update cue displays for this cue if not in live view

					if(!GUI.this.e.showLive()) {
						GUI.this.e.updateChDisplays();
						GUI.this.e.updateGUIEventPanel();					
					}

					f.validate();
				}
			}
		});

		//		JScrollPane listScroller = new JScrollPane(list);
		//		listScroller.setPreferredSize(new Dimension(150, 40));
		//		leftPanel.add(new JLabel("Cue List"));
		//		leftPanel.add(listScroller);
		//		p.add(leftPanel, BorderLayout.LINE_START); 
		p.add(list, BorderLayout.LINE_START); 

		start = new JButton("Play");
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleButtonClick((JButton)e.getSource());
			}
		});

		reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Call restTime with percent set to 0
				editor.resetTimer(0);

				//Move slider to 0
				setSliderPosition(0);
			}
		});

		addCue = new JButton("Add Cue");
		addCue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.addNewCue();
			}
		});

		f.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
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

		editCue = new JButton("Edit Cue");
		//Will only be enabled when a cue is selected
		editCue.setEnabled(false);
		editCue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Cue tmp = (Cue) list.getSelectedValue();
				if(tmp != null) {
					editor.editCue(tmp);
				}
				else System.err.println("Error: No cue select");
			}
		});

		removeCue = new JButton("Remove Cue");
		//Will only be enabled when a cue is selected
		removeCue.setEnabled(false);
		removeCue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Cue tmp = (Cue) list.getSelectedValue();

				if(tmp != null) {
					int confirm = JOptionPane.showConfirmDialog(p, "Remove cue " + tmp.toString()+ "?", "Remove Cue", JOptionPane.OK_CANCEL_OPTION);
					if(confirm == JOptionPane.OK_OPTION) {
						System.out.println("Removing cue at " + tmp.getRunTime());
						if (e.removeCue(tmp)) {
							System.out.println("Cue removed");
						}
						else {
							System.err.println("Unable to remove cue");
						}
					}
				}
				else System.err.println("Error: No cue select");
			}
		});
		
		openEffectsTimer = new JButton("Effects Timer");
		openEffectsTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor.openEffectsTimer();
			}
		});

		setLiveBox = new JCheckBox();
		editor.setShowLive(false); //Initially unchecked
		setLiveBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				if(setLiveBox.isSelected()) {
					editor.setShowLive(true);
				}
				else editor.setShowLive(false);

				editor.updateChDisplays();
				editor.updateGUIEventPanel();
			}
		});

		audioSlider = new JSlider(0, sliderMaxVal, 0);
		audioSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				//				System.out.println("Adjusting: " + audioSlider.getValueIsAdjusting());
				//				System.out.println(getSliderValue());
				//				System.out.println(audioSlider.getValue());
				//				System.out.println();

				//Make sure change is made by user
				if(audioSlider.isEnabled()){
					//					System.out.println("Slider val: " + audioSlider.getValue());
					editor.resetTimer(getSliderValue());
				}
			}
		});

		eventPanel = new JPanel();
		eventScrlPane = new JScrollPane(eventPanel);

		//Initialize button panel
		buttonPanel = new JPanel();
		topButtonPanel = new JPanel();
		bottomButtonPanel = new JPanel();		

		topButtonPanel.add(timeText);
		topButtonPanel.add(audioSlider);
		topButtonPanel.add(start);
		topButtonPanel.add(reset);
		topButtonPanel.add(new JLabel("Show Live: "));
		topButtonPanel.add(setLiveBox);

		bottomButtonPanel.add(addCue);
		bottomButtonPanel.add(editCue);
		bottomButtonPanel.add(removeCue);
		bottomButtonPanel.add(openEffectsTimer);

		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(topButtonPanel);
		buttonPanel.add(bottomButtonPanel);

		p.add(buttonPanel, BorderLayout.PAGE_END);

		//Initialize statePanel
		statePanel = new JPanel();
		//		statePanel.add(new JTextField("Current Channel States"));


		p.add(statePanel, BorderLayout.CENTER);

		f.validate();
	}

	protected void handleButtonClick(JButton b) {
		if(b.getText() == "Play") {
			System.out.println("Play button pressed");
			e.startTimer();
		}
		else if (b.getText() == "Stop") {
			e.stopTimer();
		}
	}

	void updateTime() {
		double timeInSec = Math.floor((e.getEditorTime() / 10)) / 100.0; //displaying with two decimal places

		timeText.setText("Time: " + timeInSec + " s");	
		f.validate();
	}

	void printCues() {
		this.cues= e.getCurrentSong().getCues();
		list.clearSelection();
		list.setListData(cues);
		list.validate();

		//		//persist currently selected cue
		if(e.getSelectedCue() != null) {
			list.setSelectedValue(e.getSelectedCue(), true);
		}
		
		//if no cue is selected, disable edit and remove cue buttons
		if(list.getSelectedValue() == null){
			editCue.setEnabled(false);
			removeCue.setEnabled(false);
			e.setSelectedCue(null);
		}



		//Disable remove cue since no cue will be selected
		//		removeCue.setEnabled(false);
	}

	protected void updateEventPanel(Cue cue) {
		//Clear current panel
		eventPanel.removeAll();
		eventScrlPane.setVisible(false);
		p.remove(eventScrlPane);

		//Create event info panel
		eventPanel = new JPanel();
		eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
		eventPanel.setBackground(Color.WHITE);
		eventScrlPane = new JScrollPane(eventPanel);
		p.add(eventScrlPane, BorderLayout.LINE_END);

		if(cue != null){
			eventPanel.add(new JLabel("Time: " + cue.getRuntTimeInSecs()));
			eventPanel.add(Box.createVerticalStrut(15));
			eventPanel.add(new JLabel("# of Events: " + cue.getEvents().size()));

			if(cue.getEvents().size()>0){
				ArrayList<LightEvent> evs = cue.getEvents();
				for(int i=0; i<evs.size(); i++) {
					eventPanel.add(Box.createVerticalStrut(15));
					LightEvent ev = evs.get(i);

					eventPanel.add(new JLabel("Event: " + i));
					eventPanel.add(new  JLabel("Channel: " + ev.getChannel().getChName()));
					eventPanel.add(new  JLabel("Channel #: " + ev.getChannel().getChNum()));

					if(ev.isEffect()) {
						eventPanel.add(new  JLabel("State: Effect"));
						eventPanel.add(new JLabel("Effect Rate "+ ev.getEffectRateInSecs()));
					}

					else if (ev.isOn()){

						eventPanel.add(new  JLabel("State: On"));

					}
					else {
						eventPanel.add(new  JLabel("State: Off"));
					}
				}
			}
		}

		//refresh panel
		p.validate();
	}


	//	public void removeHighlightCue(Cue currentCue) {
	//	}
	//	
	//	public void highlightCue(Cue nextCue) {
	//		list.setCellRenderer(new CueListRenderer());
	//	}
	protected void printStates() {
		statePanel.removeAll();
		statePanel.setLayout(new GridLayout(4, 2));
		//		statePanel.add(new JLabel("Current Channel States"));

		//		f.add(statePanel, BorderLayout.CENTER);


		for(Channel ch: e.getCurrentSong().getChannels()) {
			JPanel chStatePanel = new JPanel();
			chStatePanel.setLayout(new BoxLayout(chStatePanel, BoxLayout.PAGE_AXIS));
			chStatePanel.add(new JLabel(ch.getChNum() +": " + ch.getChName()));
			chStatePanel.add(new JLabel("State: " + ch.getCurrentStateString()));

			if(ch.getCurrentState() == LightEvent.ON_STATE) {
				chStatePanel.setBackground(ch.getColor());
			}
			else if (ch.getCurrentState() == LightEvent.EFFECT_STATE) {
				chStatePanel.add(new JLabel("Effect Rate: " + ch.getCurrentEffectRateInSecs()));
				chStatePanel.setBackground(ch.getColor());
			}
			else if (ch.getCurrentState() == LightEvent.OFF_STATE) {
				chStatePanel.setBackground(Color.gray);
			}
			else System.err.println("Error: invalid current state");

			chStatePanel.add(new JLabel("Cue Last Changed: " + ch.getCueLastChanged().toString()));
			statePanel.add(chStatePanel);
		}
		//		statePanel.validate();
	}

	public double getSliderValue() {
		//		System.out.println(audioSlider.getValue());
		return (double) audioSlider.getValue() / sliderMaxVal;
	}

	public boolean setSliderPosition(double perc) {
		if(perc >= 0 && perc <=1){
			//			System.out.println("Perc: " + perc);
			audioSlider.setValue((int) Math.floor(perc * 100));
			audioSlider.validate();
			return true;
		}
		else {
			System.err.println("Error: Invalid percent in setSliderPosition");
			return false;
		}
	}

	public void togglePlayButton() {
		if(start.getText() == "Play") {
			start.setText("Stop");

			//Disable slider from being moved by user
			reset.setEnabled(false);
			audioSlider.setEnabled(false);
		}
		else {
			start.setText("Play");

			//Enable slider and reset
			reset.setEnabled(true);
			audioSlider.setEnabled(true);
		}
	}
}
