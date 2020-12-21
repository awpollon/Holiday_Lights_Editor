import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;



public class Cue implements Comparable<Cue>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7498455730594590728L;
	
	private double runTime;
	@JsonIgnore
	private transient boolean isActive; //Is the current cue in playback
	
	private ArrayList<LightEvent> events;
	public ArrayList<LightEvent> getEvents() {
		return events;
	}

	public Cue(double time) {
		this.setRunTime(time);
		events = new ArrayList<LightEvent>();
		this.isActive = false;
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
		return (this.getRuntTimeInSecs() + "");
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean active) {
		this.isActive = active;
	}

	@JsonIgnore()
	public double getRuntTimeInSecs(){
		return (Math.floor((runTime /10)) /100.0);
	}
	

}

