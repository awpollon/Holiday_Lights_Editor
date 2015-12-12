import java.io.Serializable;

public class LightEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4635984324931825931L;

	public static final int ON_STATE = 1;
	public static final int OFF_STATE = 2;
	public static final int EFFECT_STATE = 3;

	
	private Channel channel;
	private boolean on;
	private boolean isEffect;
//	private int effectRate;
	private long effectOn = 0;
	private long effectOff = 0;
	private int state;

	public LightEvent(Channel c, boolean turnOn, boolean isEffect, long effectOnRate, long effectOffRate) {
		this.channel = c;
		this.on = turnOn;
		
		this.isEffect = isEffect;
		
		if(this.isEffect) {
			this.effectOn = effectOnRate;
			this.effectOff = effectOffRate;
//			this.effectRate = effectRate;
			this.state = EFFECT_STATE;
		}
		else {
//			effectRate = 0;	
			if(turnOn) this.state = ON_STATE;
			else this.state = OFF_STATE;
		}
	}

//	public int getEffectRate() {
//		return effectRate;
//	}

	public Channel getChannel() {
		return channel;
	}

	public boolean isEffect() {
		return isEffect;
	}

	public boolean isOn() {
		return on;
	}

//	public double getEffectRateInSecs() {
//		return effectRate / 1000.0;
//	}
	
	public long getEffectOnRate(){
		return effectOn;
	}
	
	public long getEffectOffRate(){
		return effectOff;
	}

	public int getState() {
		return state;
	}

	public void setState() {
		//Sets state based on boolean values
		if(this.isEffect) {
			this.state = EFFECT_STATE;
		}
		else {
			if(this.on) this.state = ON_STATE;
			else this.state = OFF_STATE;
		}
	}
}
