import java.io.Serializable;


public class Channel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7257850489035330987L;
	
	private String chName;
	private String chVar;
	private String color;
	private int chNum;
	private int numLights;
	private int arduinoPin;
	
	public Channel(String name, int channel, int pin) {
		this.setChName(name);
		this.setColor(color);
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
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
}
