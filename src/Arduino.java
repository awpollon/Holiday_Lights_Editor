import java.io.BufferedWriter;


public class Arduino {
	public static boolean digitalWrite(BufferedWriter bw, int pin, boolean on) {
		String state = "HIGH";
		if(!on) state = "LOW";

		
		try {
			bw.append("digitalWrite(" +pin + ", " + state + ");\n");
			return true;
		}
		catch (Exception e) {
			System.err.println("Unable to write state to file");
			return false;
		}
	}
	
	public static boolean writeDelay(BufferedWriter bw, double delay) {
		try {
			bw.append("delay(" + delay + ");\n");
			return true;
		}
		catch (Exception e) {
			System.err.println("Unable to write delay to file");
			return false;
		}
	}
}
