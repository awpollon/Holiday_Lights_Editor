import java.io.Serializable;
import java.util.ArrayList;


public class CueList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9162876658074854677L;
	
	private Cue root;
	private int numCues;

	public CueList(double songDur) {
		root = new Cue(Math.floor(songDur /2));
		root.leftQ = null;
		root.rightQ = null;
		root.isRoot = true; //Marks as root so program ignores
		this.numCues = 0;
	}

	public int getNumCues() {
		return numCues;
	}

	public boolean addCue(Cue c) {
		return this.insertCue(c, this.root);
	}

	public ArrayList<Cue> getAllCues(){
		return traverse(this.root, new ArrayList<Cue>());
	}


	private ArrayList<Cue> traverse(Cue c, ArrayList<Cue> list) {
		if(c.leftQ != null){
			traverse(c.leftQ, list);
		}
		if(!c.isRoot) {
			System.out.println(c.getRunTime());
			list.add(c);
		}
		if (c.rightQ != null){
			traverse(c.rightQ, list);
		}
		return list;
	}

	private boolean insertCue(Cue c, Cue root) {
		if(root.getRunTime() == c.getRunTime()) {
			//Check if actual root (not a real cue)
			if(root.isRoot) {
				c.leftQ = root.leftQ;
				c.rightQ = root.rightQ;
				this.root = c;
				
				this.numCues++;
			}
			System.out.println("Unable to insert Cue: Cue already exists at that time");
			return false;
		}
		else if(c.getRunTime() < root.getRunTime()){
			if(root.leftQ != null) return insertCue(c, root.leftQ);
			else {
				//Insert cue to the left of current traverses root
				this.numCues++;
				root.leftQ = c;
				c.parent = root;
				return true;
			}
		}
		else if (c.getRunTime() > root.getRunTime()){
			if( root.rightQ != null) return insertCue(c, root.rightQ);
			else {
				this.numCues++;
				root.rightQ = c;
				c.parent = root;
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		CueList cl = new CueList(10000);
		for (int i =0; i<100; i++) {
			cl.addCue(new Cue(Math.floor(Math.random()*10000)));
		}
		cl.addCue(new Cue(5000));
		Cue g = cl.getCue(1121);
		
		System.out.println("Num cues: " + cl.numCues);
		cl.getAllCues();
	}

	public boolean remove(Cue c, Cue root) {
		if(this.numCues==0) {
			System.out.println("Cannot remove: no");
			return false;
		}

		this.numCues--;
		return false;
	}

	public Cue getCue(double time) {
		return returnCue(time, this.root);
	}

	private Cue returnCue(double time, Cue root) {
		if(root.getRunTime() == time) {
			return root;
		}
		else if(time < root.getRunTime()){
			if(root.leftQ != null) return returnCue(time, root.leftQ);
		}
		else if (time > root.getRunTime()){
			if( root.rightQ != null) return returnCue(time, root.rightQ);

		}

		System.out.println("Cue not found.");
		return null;
	}
	

}

