import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LightEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4635984324931825931L;

	public static final int ON_STATE = 1;
	public static final int OFF_STATE = 2;
	public static final int EFFECT_STATE = 3;

	@JsonIgnore
	private Channel channel; // TODO: Remove

	private int channelNum;

	@JsonIgnore
	private boolean on;

	@JsonIgnore
	private boolean isEffect;

	private int effectRate;
	private int state;

	public LightEvent() {
	}

	public LightEvent(int channelNum, boolean turnOn, boolean isEffect, int effectRate) {
		this.channelNum = channelNum;

		this.isEffect = isEffect;
		
		if (this.isEffect) {
			this.effectRate = effectRate;
			this.state = EFFECT_STATE;
		} else {
			this.effectRate = 0;
			this.state = turnOn ? ON_STATE : OFF_STATE;
		}
	}

	public int getEffectRate() {
		return effectRate;
	}

	public int getChannelNum() {
		return channelNum;
	}

	@JsonIgnore
	public boolean isEffect() {
		return this.state == EFFECT_STATE;
	}

	@JsonIgnore
	public boolean isOn() {
		return this.state == ON_STATE;
	}

	@JsonIgnore
	public double getEffectRateInSecs() {
		return effectRate / 1000.0;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void fixState() {
		//Sets state based on boolean values
		if (this.isEffect) {
			this.state = EFFECT_STATE;
		}
		else {
			if(this.on) this.state = ON_STATE;
			else this.state = OFF_STATE;
		}
	}

	public void migrateLegacy() {
		// Migrate legacy class
		if (this.channel != null) {
			this.channelNum = this.channel.getChNum();
			this.channel = null;
		}

		fixState();
	}
}
