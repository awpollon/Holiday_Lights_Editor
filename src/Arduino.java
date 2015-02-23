import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Arduino {
	public static boolean writeIntro(BufferedWriter  bw) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		
		try {
			bw.append("/*\n");
			bw.append("This code has been generated by Song_Creater.java and is intended for an Arduino. \n"); 
			bw.append("Created by Aaron Pollon\n");
			bw.append("Created on " + dateFormat.format(cal.getTime()) + "\n");
			bw.append("*/\n\n");
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean writeDefines(BufferedWriter  bw, Object[] chs) {

		try {			
			for(int i=0; i<chs.length; i++) {
				if(chs[i] != null){
					Channel c = (Channel) chs[i];
					bw.append("#define " + c.getChVar() + " " +c.getArduinoPin() + "\n");
				}
			}
			bw.append("\n");
			return true;
		}

		catch(Exception e) {
			System.err.println("Error: Unable to print cues");
			return false;
		}
	}

	public static boolean writeSetup(BufferedWriter  bw, Object[] chs) {
		try {
			bw.append("void setup() {\n");
			bw.append("Serial.begin(9600);\n");

			for(int i=0; i<chs.length; i++) {
				if(chs[i] != null){
					Channel c = (Channel) chs[i];
					bw.append("pinMode(" + c.getChVar() +", OUTPUT);\n");
				}
			}

			bw.append("Serial.print(\"Setup Complete\");\n");
			bw.append("delay(2000);\n");
			bw.append("}\n\n");
			return true;
		}

		catch(Exception e) {
			System.err.println("Error: Unable to print cues");
			return false;
		}
	}


	public static boolean digitalWrite(BufferedWriter bw, Channel c, boolean on) {
		String state = "HIGH";
		if(!on) state = "LOW";


		try {
			bw.append("digitalWrite(" +c.getChVar() + ", " + state + ");\n");
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
	public static boolean writeCountdown(BufferedWriter bw, int start) {
		try {
			bw.append("Serial.println(\"Starting...\");\n");
			bw.append("for (int i = 0; i<" + start + "; i++){\n");
			bw.append("Serial.println("+start + "-i);\n");
			bw.append("delay(1000);\n");
			bw.append("}\n");
			bw.append("Serial.println(\"GO!\");\n");
			bw.append("\n\n");

			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	public static boolean printToSerial(BufferedWriter bw, String out){
		try {
			bw.append("Serial.println(\""+out+"\");\n");
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
}


