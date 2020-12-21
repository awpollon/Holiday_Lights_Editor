import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.Color;
import java.io.Serializable;


public class Channel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7257850489035330987L;
	
	private String chName;
	private String chVar;

	@JsonIgnore
	private Color color;
	//TODO: Map a color enum list

	private int chNum;
	private int numLights;
	private int arduinoPin;

	@JsonIgnore
	private transient int currentState; //storing current state in the editor
	@JsonIgnore
	private transient Cue cueStateLastChanged;
	@JsonIgnore
	private transient double currentEffectRate;
	
	public Channel(String name, int channel, int pin, Color col) {
		this.setChName(name);
		this.setColor(col);
		this.setChNum(channel);
		this.setNumLights(numLights);
		this.setArduinoPin(pin);
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
		this.chVar = chName.replaceAll("\\s+","");
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getChNum() {
		return chNum;
	}

	public void setChNum(int chNum) {
		this.chNum = chNum;
	}

	public int getNumLights() {
		return numLights;
	}

	public void setNumLights(int numLights) {
		this.numLights = numLights;
	}

	public int getArduinoPin() {
		return arduinoPin;
	}

	public void setArduinoPin(int arduinoPin) {
		this.arduinoPin = arduinoPin;
	}
	
	@Override
	public String toString() {
		return (chNum + ": " + chName);
	}

	public String getChVar() {
		return chVar;
	}

	@JsonIgnore
	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int i, Cue qLastChanged, double effectRate) {
		this.currentState = i;
		this.cueStateLastChanged = qLastChanged;
		this.currentEffectRate = effectRate;
	}

	@JsonIgnore
	public String getCurrentStateString() {
		if(this.currentState == LightEvent.ON_STATE) return EventInput.onText;
		else if(this.currentState == LightEvent.OFF_STATE) return EventInput.offText;
		else if (this.currentState == LightEvent.EFFECT_STATE) return EventInput.effectText;
		else return null;
	}

	public Cue getCueLastChanged() {
		return cueStateLastChanged;
	}

	public double getCurrentEffectRate() {
		return currentEffectRate;
	}
	@JsonIgnore
	public double getCurrentEffectRateInSecs() {
		return currentEffectRate / 1000.0;
	}
}
