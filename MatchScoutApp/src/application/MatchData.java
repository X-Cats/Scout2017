package application;
/**
 * Stores Match Data to be exported into various formats.
 * @author alfs
 *
 */
public class MatchData {
	private String teamName;
	private int teamNumber;
	private int matchNumber;
	private boolean alliance; //True for Red, False for blue.
	private boolean autoBaselineX;
	private boolean autoHighFuel, autoLowFuel;
	private int kPaAuto;
	private boolean autoLeftGear, autoLeftGearFailed;
	private boolean autoCenterGear, autoCenterGearFailed;
	private boolean autoRightGear, autoRightGearFailed;
	private int matchGearsPlaced, matchGearsFailed;
	private int kPaScored;
	private boolean climbed, climbFailed, climbNoAttempt, climbMalfunction;
	private String notes;
	/**
	 * Creates a new MatchData object containing data for a certain match
	 * @param name Name of the team
	 * @param teamNumber Number of the team
	 * @param matchNumber Match number
	 * @param alliance Which alliance the team is on (true = red, false = blue)
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
	public MatchData(String name, int teamNumber, int matchNumber, boolean alliance, boolean autoBaseline, boolean autoHighFuel, boolean autoLowFuel, int kPaAuto,
			boolean autoLeftGear, boolean autoLeftGearFailed, boolean autoCenterGear, boolean autoCenterGearFailed,
			boolean autoRightGear, boolean autoRightGearFailed, int matchGearsPlaced, int matchGearsFailed, int kPaScored,
			boolean climbed, boolean climbFailed, boolean climbNoAttempt, boolean climbMalfunction, String notes) {
		this.teamName = name;
		this.teamNumber = teamNumber;
		this.matchNumber = matchNumber;
		this.alliance = alliance;
		this.autoBaselineX = autoBaseline;
		this.autoHighFuel = autoHighFuel;
		this.autoLowFuel = autoLowFuel;
		this.kPaAuto = kPaAuto;
		this.autoLeftGear = autoLeftGear;
		this.autoLeftGearFailed = autoLeftGearFailed;
		this.autoCenterGear = autoCenterGear;
		this.autoCenterGearFailed = autoCenterGearFailed;
		this.autoRightGear = autoRightGear;
		this.autoRightGearFailed = autoRightGearFailed;
		this.matchGearsPlaced = matchGearsPlaced;
		this.matchGearsFailed = matchGearsFailed;
		this.kPaScored = kPaScored;
		this.climbed = climbed;
		this.climbFailed = climbFailed;
		this.climbNoAttempt = climbNoAttempt;
		this.climbMalfunction = climbMalfunction;
		this.notes = notes;
	}
	/**
	 * Converts the MatchData object into a CSV string
	 * @return A CSV string containing the data from the MatchData object
	 */
	public String toCSV() { //TODO Look at database and figure out how to properly write this; then properly write it
		String out = null;
		StringBuilder sb = new StringBuilder();
		sb.append(teamName + ",");
		sb.append(teamNumber + ",");
		sb.append(matchNumber + ",");
		sb.append(boolToAlliance() + ",");
		sb.append(autoBaselineX + ",");
		sb.append(autoHighFuel + ",");
		sb.append(autoLowFuel + ",");
		sb.append(kPaAuto + ",");
		sb.append(autoLeftGear + ",");
		sb.append(autoLeftGearFailed + ",");
		sb.append(autoRightGear + ", ");
		sb.append(autoRightGearFailed + ",");
		sb.append(autoCenterGear + ",");
		sb.append(autoCenterGearFailed + ",");
		sb.append(matchGearsPlaced + ",");
		sb.append(matchGearsFailed + ",");
		sb.append(kPaScored + ",");
		sb.append(climbed + ",");
		sb.append(climbFailed + ",");
		sb.append(climbNoAttempt + ",");
		sb.append(climbMalfunction + ",");
		try {
			sb.append(notes);
		}catch(NullPointerException e) {
			sb.append(" ");
		}
		
		out = sb.toString();
		return out;
	}
	/**
	 * Turns the alliance boolean into a string containing either "red" or "blue"
	 * @return A string indicating which alliance the team was on
	 */
	private String boolToAlliance() {
		if(alliance = true) {
			return "red";
		}
		else {
			return "blue";
		}
	}
}
