package main.java;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class KeyTimer {
	private long timeStarted;
	private boolean hasFirstKey = false;
	private KeyEvent firstKeyEvent;
	private KeyEvent lastKeyEvent;
	private long timeStopped;

	private boolean printEventsLive = false;
	private boolean printEventsStop = false;

	private List<List<KeyEvent>> keyEvents;
	private Map<Integer, Integer> keyEventIndexMap; //<key code, array index>

	//GUI elements
	private JFrame frame;
	private JButton startButton;
	private JTextArea output;
	private JButton clearButton;

	public KeyTimer() {
		this.setUpWindow();
	}

	void start(){
		timeStarted = System.currentTimeMillis();
		output.append("Timer started.\n");

		keyEventIndexMap = new HashMap<Integer, Integer>();
		keyEvents = new ArrayList<List<KeyEvent>>();
	}

	void stop(){
		timeStopped = System.currentTimeMillis();
		double runTimeSecs = (timeStopped-timeStarted) / 1000.00;

		output.append("Timer stopped. Ran for " + runTimeSecs + " secs. \n");
		if(firstKeyEvent == null){
			output.append("No key events detected.\n");
		}
		else printOutput();

		output.append("---------------------------------\n");
	}

	private void printOutput() {

		for(List<KeyEvent> key : keyEvents) {
			String keyText = KeyEvent.getKeyText(key.get(0).getKeyCode());

			if(printEventsStop) output.append("Events for key: " + KeyEvent.getKeyText(key.get(0).getKeyCode()) + " :\n");
			long totalGapTime = 0;
			long startTimeIn = 0;
			long startTimeSinceFirst = 0;

			KeyEvent prevEvent = null;

			for(KeyEvent event: key) {
				double eventTimeSecs = (event.getWhen() - timeStarted) / 1000.00;

				if(printEventsStop) output.append("time: " + eventTimeSecs + ", key: " + keyText + "\n");
				if(key.indexOf(event) > 0) {
					totalGapTime += (event.getWhen() - prevEvent.getWhen());
				}
				else{
					startTimeSinceFirst = (event.getWhen() - firstKeyEvent.getWhen());
					startTimeIn= (event.getWhen() - timeStarted);
				}
				prevEvent = event;
			}
			output.append("\n");
			double averageGap = ((totalGapTime / (long) key.size()) / 1000.00);
			output.append("Average rate for " + keyText + ": " + averageGap + "\n");
			if(startTimeSinceFirst == 0.0){
				output.append("First key pressed, at " + (startTimeIn / 1000.0) + " secs since timer start.\n");
			}
			else output.append("First press at " + (startTimeSinceFirst / 1000.00) + " secs since first key.\n");

		}
		double timeFirstToLast = ((lastKeyEvent.getWhen() - firstKeyEvent.getWhen()) / 1000.00);
		double totalTime = ((timeStopped - timeStarted) / 1000.00);

		output.append("Time first to last event: " + timeFirstToLast + "\n");
		output.append("Total time: " + totalTime + "\n");		
	}

	private void setUpWindow(){
		//Create and set up the window.
		frame = new JFrame("Effects Timer");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		//Set up the content pane.
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleStartButtonClick((JButton)e.getSource());
			}
		});

		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearOutput();
			}
		});


		frame.setSize(350, 210);
		frame.setFocusable(true);
		startButton.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				recordKeyPress(e);	
			}
		});

		output = new JTextArea();
		output.setEditable(false);
		output.setFocusable(false);
		JScrollPane scrollPane = new JScrollPane(output);
		scrollPane.setPreferredSize(new Dimension(350, 150));

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(startButton);
		buttonPanel.add(clearButton);

		frame.add(buttonPanel, BorderLayout.PAGE_START);
		frame.add(scrollPane, BorderLayout.PAGE_END);


		//Display the window.
		//        frame.pack();
		frame.setVisible(true);
	}

	private void handleStartButtonClick(JButton source) {
		if(startButton.getText().equals("Start")) {
			start();
			startButton.setText("Stop");
		}
		else{
			stop();
			startButton.setText("Start");
		}
	}

	private void recordKeyPress(KeyEvent e){
		if(!hasFirstKey){
			hasFirstKey = true;
			firstKeyEvent = e;
		}

		lastKeyEvent = e;


		if(keyEventIndexMap.containsKey(e.getKeyCode())) {
			//Check if key has already been pressed
			int keyIndex = keyEventIndexMap.get(e.getKeyCode());
			keyEvents.get(keyIndex).add(e);
		}
		else {
			//If not, create new arraylist and register index in map
			ArrayList<KeyEvent> newList = new ArrayList<KeyEvent>();
			newList.add(e);
			keyEvents.add(newList);
			int newListIndex = keyEvents.indexOf(newList);
			keyEventIndexMap.put(e.getKeyCode(), newListIndex);
		}
		
	}
	
	public void outputLine(String out) {
		output.append(out + "\n");
	}
	public void clearOutput() {
		output.setText("");;
	}

	public static void main(String[] args) {
		//Main method for testing
		KeyTimer et = new KeyTimer();
		et.setUpWindow();
	}

}
