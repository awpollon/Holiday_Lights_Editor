import java.awt.Color;
import java.io.Serializable;


public class Channel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7257850489035330987L;
	
	private String chName;
	private String chVar;
	private Color color;
	private int chNum;
	private int arduinoPin;
	
	private transient int currentState; //storing current state in the editor

	private transient Cue cueStateLastChanged;

	private transient double currentEffectRate;
	
	public Channel(String name, int channel, int pin, Color col) {
		this.setChName(name);
		this.setColor(col);
		this.setChNum(channel);
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

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int i, Cue qLastChanged, double effectRate) {
		this.currentState = i;
		this.cueStateLastChanged = qLastChanged;
		this.currentEffectRate = effectRate;
	}

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
	
	public double getCurrentEffectRateInSecs() {
		return currentEffectRate / 1000.0;
	}
}
