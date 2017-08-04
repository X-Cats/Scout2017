package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Takes data and puts it into a <code>MatchData</code> object. Stores an array of all the <code>MatchData</code> and handles turning it into a CSV format.
 * Keeps track of what has been written to a file and removes it from the array of <code>MatchData</code> that hasn't been written (the <code>notWritten</code> array) while
 * keeping all the data in an array called <code>allData</code>
 * 
 * @author Aidan Sciortino
 *
 */
public class DataHandler {
	
	private static ArrayList<MatchData> notWritten = new ArrayList<MatchData>();
	private static ArrayList<MatchData> allData = new ArrayList<MatchData>();
	
	/**
	 * Sterilizes a string for a CSV file, removing characters such as ','
	 * 
	 * @param input The string to be sterilized
	 * @return The sterilized input string
	 */
	public static String removeBadChars(String input) {
		try{
			char[] inputChar = input.toCharArray();
			StringBuilder sb = new StringBuilder();
			for (char i : inputChar) {
				if (i == ',' || i == ';')
					sb.append(".");
				else
					sb.append(i);
			}
			return sb.toString();
		}catch(NullPointerException e) {
			return "none";
		}
	}
	/**
	 * Takes all the data in the <code>notWritten</code> array and turns it into a CSV string, each data point separated into lines
	 * 
	 * @return One line of CSV data for each <code>MatchData</code> object in the <code>notWritten</code> array.
	 */
	public static String allToCSV() {
		String out = "";
		for(MatchData md:notWritten) {
			out += md.toCSV() + "\n";
		}
		notWritten = new ArrayList<MatchData>();
		return out;
	}
	/**
	 * Adds a new <code>MatchData</code> object to the <code>notWritten</code> array.
	 * 
	 * @param name Name of the team for the MatchData Object
	 * @param teamNumber Number of the team for the MatchData Object
	 * @param matchNumber Match number for the MatchData Object
	 * @param alliance Which alliance the team is on for the MatchData object (true = red, false = blue)
	 * @param autoBaseline Whether or not the team crosses the baseline this match
	 * @param autoHighFuel Whether or not the team shot fuel into the high goal in auto this match
	 * @param autoLowFuel Whether or not the team shot fuel into the low goal in auto this match
	 * @param kPaAuto How many kPa the team scored in auto this match
	 * @param autoLeftGear Whether or not the team went for the left gear peg in auto this match
	 * @param autoLeftGearFailed Whether or not they failed the left gear peg in auto
	 * @param autoCenterGear Whether or not the team went for the center gear peg in auto this match
	 * @param autoCenterGearFailed Whether or not they failed the center gear peg in auto
	 * @param autoRightGear Whether or not the team went for the right gear peg in auto this match
	 * @param autoRightGearFailed Whether or not they failed the right gear peg in auto
	 * @param matchGearsPlaced How many gears the team placed this match
	 * @param matchGearsFailed How many gears the team failed to place this match
	 * @param kPaScored How many kPa the team scored this match
	 * @param climbed Whether or not the team climbed this match
	 * @param climbFailed Whether or not the team attempted the climb but didn't activate the touchpad
	 * @param climbNoAttempt Whether or not the team did not attempt to climb this match
	 * @param climbMalfunction Whether or not the team had a malfunction with their climber this match
	 * @param notes Further notes on a team's match performance
	 */
	public static void addData(String name, int teamNumber, int matchNumber, boolean alliance, boolean autoBaseline, boolean autoHighFuel, boolean autoLowFuel, int kPaAuto,
			boolean autoLeftGear, boolean autoLeftGearFailed, boolean autoCenterGear, boolean autoCenterGearFailed,
			boolean autoRightGear, boolean autoRightGearFailed, int matchGearsPlaced, int matchGearsFailed, int kPaScored,
			boolean climbed, boolean climbFailed, boolean climbNoAttempt, boolean climbMalfunction, String notes) {
		MatchData md = new MatchData(name, teamNumber, matchNumber, alliance, autoBaseline, autoHighFuel,
				autoLowFuel, kPaAuto, autoLeftGear, autoLeftGearFailed, autoCenterGear, autoCenterGearFailed,autoRightGear,
				autoRightGearFailed, matchGearsPlaced,matchGearsFailed,kPaScored, climbed, climbFailed, climbNoAttempt,
				climbMalfunction,removeBadChars(notes));
		notWritten.add(md);
		allData.add(md);
		System.out.println("Data Written");
	}
}
