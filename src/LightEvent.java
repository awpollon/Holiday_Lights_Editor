import java.io.Serializable;

public class LightEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4635984324931825931L;

	private Channel channel;
	private boolean on;
	private boolean isEffect;
	private int effectRate;

	public LightEvent(Channel c, boolean turnOn, boolean isEffect, int effectRate) {
		this.channel = c;
		this.on = turnOn;
		
		this.isEffect = isEffect;
		
		if(this.isEffect) {
			this.effectRate = effectRate;
		}
		else effectRate = 0;
	}

	public int getEffectRate() {
		// TODO Auto-generated method stub
		return effectRate;
	}

	public Channel getChannel() {
		return channel;
	}

	public boolean isEffect() {
		return isEffect;
	}

	public boolean isOn() {
		return on;
	}
}
