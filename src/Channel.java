
public class Channel {
	private String chName;
	private String color;
	private int chNum;
	private int numLights;
	private int arduinoPin;
	
	public Channel(String name, String color, int channel, int numLights, int pin) {
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
}
