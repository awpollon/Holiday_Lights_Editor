import java.io.Serializable;

public class LightEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4635984324931825931L;

	Channel channel;
	boolean on;

	public LightEvent(Channel c, boolean turnOn) {
		this.channel = c;
		this.on = turnOn;
	}
}
