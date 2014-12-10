
 
public class Show{
	int numSlots = 13;
	Channel[] channels;	
	Song x;

	
	public Show() {
		channels = new Channel[8];
	}
	
	public static void main(String[] args) {
		Show s = new Show();

		
		Song jingle = new Song("Jingle Bells", s);
		s.channels[1] = new Channel("Reds", "red", 1, 100, 10);
		s.channels[2] = new Channel("Blues", "blue", 2, 100, 11);

		Cue c = new Cue(0);
		c.addEvent(new LightEvent(s.channels[1], true));
		c.addEvent(new LightEvent(s.channels[2], true));
		jingle.addCue(c);
		
		Cue d = new Cue(2000);
		d.addEvent(new LightEvent(s.channels[1], false));
		jingle.addCue(d);
		
		jingle.writeFile();
	}
}