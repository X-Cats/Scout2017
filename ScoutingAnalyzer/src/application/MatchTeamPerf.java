package application;

public class MatchTeamPerf {
	private int matchNumber;
	private String teamName;
	private int teamNumber;
	private int gearsScored;
	private int ballsScored;
	public MatchTeamPerf(int matchnum, String teamname, int teamnum, int gearsScored,int kpaScored){
		this.matchNumber = matchnum;
		this.teamName = teamname;
		this.teamNumber = teamnum;
		this.gearsScored = gearsScored;
		this.ballsScored = kpaScored;
	}
	public int getMatchNumber() {
		return matchNumber;
	}
	public String getTeamName() {
		return teamName;
	}
	public int getTeamNumber() {
		return teamNumber;
	}
	public int getGearsScored(){
		return gearsScored;
	}
	public int getBallsScored(){
		return ballsScored;
	}
	
}
