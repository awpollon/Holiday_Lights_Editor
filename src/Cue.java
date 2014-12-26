import java.io.Serializable;
import java.util.ArrayList;


public class Cue implements Comparable<Cue>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7498455730594590728L;
	
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

	@Override
	public int compareTo(Cue c) {
		if (c.getRunTime() > this. runTime) return -1;
		else if (c.getRunTime() < this.runTime) return 1;		
		else return 0;
	}
	@Override
	public String toString() {
		return (this.runTime + "");
	}
}

