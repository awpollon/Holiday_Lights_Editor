import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.Color;
import java.io.Serializable;


public class Channel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7257850489035330987L;

	private String chName;

	@JsonIgnore
	private String chVar;

	@JsonIgnore
	private Color color; //TODO: For legacy

	private GUI.DisplayColor displayColor;

	private int chNum;
	private int arduinoPin;

	@JsonIgnore
	private transient int currentState; //storing current state in the editor
	@JsonIgnore
	private transient Cue cueStateLastChanged;
	@JsonIgnore
	private transient double currentEffectRate;

	public Channel() {
	}

	public Channel(String name, int channel, int pin, GUI.DisplayColor color) {
		this.setChName(name);
		this.chNum = channel;
		this.setArduinoPin(pin);
		this.displayColor = color;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
		this.chVar = chName.replaceAll("\\s+","");
	}

	public GUI.DisplayColor getDisplayColor() {
		return displayColor;
	}

	public void setDisplayColor(GUI.DisplayColor displayColor) {
		this.displayColor = displayColor;
	}

	public int getChNum() {
		return chNum;
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

	@JsonIgnore
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

	public void migrateLegacy() {
		if (this.color != null) {
			this.displayColor = null;
			this.color = null;
		}
	}
}
