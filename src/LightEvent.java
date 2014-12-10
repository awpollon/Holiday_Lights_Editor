public class LightEvent {
		Channel channel;
		boolean on;
		
		public LightEvent(Channel c, boolean turnOn) {
			this.channel = c;
			this.on = turnOn;
		}
}
