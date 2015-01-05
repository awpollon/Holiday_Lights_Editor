import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class GUI implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2843788118490686677L;

	Editor e;

	JTextField timeText;
	JPanel p;
	JFrame f;

	JPanel buttonPanel;
	JPanel eventPanel;
	JScrollPane eventScrlPane;


	JList list;

	JMenuBar menubar;
	JMenu file;
	JMenuItem save;
	JMenuItem saveAs;
	JMenuItem open;
	JMenu export;
	JMenuItem quit;

	JMenu edit;
	JMenuItem renameSong;
	JMenuItem editChannels;
	JMenuItem duplicateCue;

	JMenu help;
	JMenuItem info;


	JButton start;
	JButton reset;
	JButton addCue;
	JButton removeCue;

	private Object[] cues;


	public GUI(final Editor editor) {
		this.e = editor; //Specifies the editor session

		f = new JFrame("Show Editor: \"" + e.getCurrentSong().getTitle() +"\"");
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.setBounds(50, 100, 500, 400);
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
					e.createNewFile(fileName);
				}
			}
		});

		open = new JMenuItem("Open File");
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				int confirmOpen = JOptionPane.showConfirmDialog(null, "Open New File?", "Open", JOptionPane.YES_NO_OPTION);
				if (confirmOpen == JOptionPane.YES_OPTION){
					System.out.println("Openng");
					JFileChooser fc = new JFileChooser(e.getCurrentSong().getFilePath());

					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"Ser", "ser");
					fc.setFileFilter(filter);					
					boolean success = false;
					while(!success){

						int opened = fc.showDialog(null, "Open");
						if (opened == JFileChooser.APPROVE_OPTION) {
							if(e.openFile(fc.getSelectedFile())) {
								success = true;
							}
						}
						else success = true;
					}
				}
			}
		});

		export = new JMenu("Export");
		JMenuItem toSketch = new JMenuItem("Arduino Sketch");
		export.add(toSketch);

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
		editChannels = new JMenuItem("Channels");
		editChannels.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

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

		//Test: print cue list
		cues= e.getCurrentSong().getCues();


		list = new JList(e.getCurrentSong().getCues());
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setVisibleRowCount(-1);


		//		JScrollPane listScroller = new JScrollPane(list);
		p.add(list,BorderLayout.CENTER);


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
				editor.setEditorTime(0);
				updateTime();
				editor.resetTimer();
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


		removeCue = new JButton("Remove Cue");
		//Will only be enbaled when a cue is selected
		removeCue.setEnabled(false);
		removeCue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Cue tmp = (Cue) list.getSelectedValue();
				int confirm = JOptionPane.showConfirmDialog(p, "Remove cue at " + tmp.getRunTime() + " ms?", "Remove Cue", JOptionPane.OK_CANCEL_OPTION);
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
		});

		eventPanel = new JPanel();
		eventScrlPane = new JScrollPane(eventPanel);

		//Initialize button panel
		buttonPanel = new JPanel();
		buttonPanel.add(timeText);
		buttonPanel.add(start);
		buttonPanel.add(reset);

		buttonPanel.add(addCue);
		buttonPanel.add(removeCue);
		p.add(buttonPanel, BorderLayout.SOUTH);

		f.validate();

	}
	protected void handleButtonClick(JButton b) {
		if(b.getText() == "Play") {
			e.startTimer();
			reset.setEnabled(false);
			b.setText("Stop");
		}
		else if (b.getText() == "Stop") {
			e.stopTimer();
			reset.setEnabled(true);

			b.setText("Play");
		}
	}

	void updateTime() {
		timeText.setText("Time: " + e.getEditorTime() + " ms");		
	}

	void printCues() {
		this.cues= e.getCurrentSong().getCues();
		list.setVisible(false);
		list = new JList(e.getCurrentSong().getCues());

		list.setFixedCellWidth(60);

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setVisibleRowCount(-1);

		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent me) {

				JList clicked = (JList) me.getSource();

				if (me.getClickCount() >= 2) {
					int index = list.locationToIndex(me.getPoint());
					Cue selectedCue = e.getCurrentSong().getCues()[index];

					boolean success = false;
					double newTime = -1;

					String input = JOptionPane.showInputDialog("Enter new cue time");

					if (input != null){
						while(!success){
							try{
								newTime = Double.parseDouble(input);
								success = true;
							}
							catch (Exception e) {
								System.out.println("Invalid Input");
								success = false;
								input = JOptionPane.showInputDialog("Enter new cue time");
							}
						}
						//Valid input
						System.out.println("New Time: " + newTime);
						selectedCue.setRunTime(newTime);
						//Update GUI
						printCues();
					}
				}
			}
		});

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					System.out.println(e);
					//Enable remove cue button
					removeCue.setEnabled(true);

					Cue selected = (Cue) list.getSelectedValue();

					//Clear current panel
					eventPanel.removeAll();
					eventScrlPane.setVisible(false);
					p.remove(eventScrlPane);

					//Create event info panel
					eventPanel = new JPanel();
					eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
					eventPanel.setBackground(Color.WHITE);
					eventScrlPane = new JScrollPane(eventPanel);
					p.add(eventScrlPane, BorderLayout.EAST);

					eventPanel.add(new JLabel("Time: " + selected.getRunTime()));
					eventPanel.add(Box.createVerticalStrut(15));
					eventPanel.add(new JLabel("# of Events: " + selected.getEvents().size()));

					if(selected.getEvents().size()>0){
						ArrayList<LightEvent> evs = selected.getEvents();
						for(int i=0; i<evs.size(); i++) {
							eventPanel.add(Box.createVerticalStrut(15));
							LightEvent ev = evs.get(i);

							eventPanel.add(new JLabel("Event: " + i));
							eventPanel.add(new  JLabel("Channel: " + ev.channel.getChName()));
							eventPanel.add(new  JLabel("Channel #: " + ev.channel.getChNum()));

							if (ev.on){
								eventPanel.add(new  JLabel("State: On"));
							}
							else {
								eventPanel.add(new  JLabel("State: Off"));
							}
						}
					}

					f.validate();
				}
			}
		});

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));

		p.add(list, BorderLayout.WEST);

		f.validate();

		//		for (int i=0; i<cues.length; i++) {
		//			System.out.println(cues[i]);
		//		}

		//Disable remove cue since no cue will be selected
		removeCue.setEnabled(false);
	}
}
