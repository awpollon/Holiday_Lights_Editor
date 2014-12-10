import java.util.ArrayList;


public class Cue {
	private ArrayList<LightEvent> events;
	public ArrayList<LightEvent> getEvents() {
		return events;
	}

	private double runTime;
	
	public Cue(double time) {
		this.setRunTime(time);
		events = new ArrayList<LightEvent>();
	}
	
	boolean addEvent(LightEvent e) {
		return events.add(e);
	}
	
	boolean removeEvent(LightEvent e) {
		return events.remove(e);
	}

	public double getRunTime() {
		return runTime;
	}

	public void setRunTime(double runTime) {
		this.runTime = runTime;
	}

}

