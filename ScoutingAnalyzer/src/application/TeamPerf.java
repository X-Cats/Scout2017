package application;

import java.util.ArrayList;

import javafx.scene.chart.XYChart;

public class TeamPerf {
	
	//General team info
	private int teamNum;
	private String teamName;
	
	//Master list of matches the team is in. 
	private ArrayList<Integer> matches;
	private double matchsum;
	
	//Raw data
	private ArrayList<Integer> gearsPerMatch;
	private ArrayList<Boolean> climb;
	private ArrayList<Integer> balls;
	ArrayList<Boolean> autoGearCenter = new ArrayList<>();
	ArrayList<Boolean> autoGearSide = new ArrayList<>();
	
	//Arrays for holding modified data
	private ArrayList<Double> modifiedBalls = new ArrayList<>();
	private ArrayList<Double> modifiedGears = new ArrayList<>();

	//SUms of all the raw data
	private int gearSum;
	private int ballSum;
	private int climbSum;
	
	//Sums of all the modified data
	private double gearModSum;
	private double ballModSum;
	private double climbModSum;
	
	//Averages for raw data
	private double avgGear;
	private double avgBall;
	private double avgClimb;
	
	//Averages for modified data
	private double avgGearMod;
	private double avgBallMod;
	private double avgClimbMod;

	int learnRate;
	
	private int centerSum;
	private double centerModSum;
	private double sideModSum;
	private int sideSum;
	
	public TeamPerf(int teamnumber, String teamname, ArrayList<Integer> gpm,ArrayList<Integer> allMatches,ArrayList<Integer> ballkPa,ArrayList<Boolean> climb,ArrayList<Boolean> autoGearCenter, ArrayList<Boolean> autoGearSide, int learnRate){
		this.autoGearCenter = autoGearCenter;
		this.autoGearSide = autoGearSide;
		this.teamNum = teamnumber;
		this.teamName = teamname;
		this.gearsPerMatch = gpm;
		this.matches = allMatches;
		this.learnRate = learnRate;
		this.balls = ballkPa;
		this.climb = climb;
		System.out.println(climb);
		calcAllData();
	}
	public void calcAllData(){
		for(int i = 0; i < matches.size(); i++){
			int currGearNum = gearsPerMatch.get(i);
			int currBallNum = balls.get(i);
			double currGearMod = (double)currGearNum * (1.0 + (((double)i)/10.0));
			double currBallMod = (double)currBallNum * (1.0 + (((double)i)/10.0));
			gearSum += currGearNum;
			gearModSum += currGearMod;
			ballModSum += currBallMod;
			modifiedGears.add(currGearMod);
			modifiedBalls.add(currBallMod);
			if(climb.get(i)){
				climbSum += 1;
				climbModSum += 1 + (double)i/10;
			}
			if(autoGearCenter.get(i)){
				centerSum +=1;
				centerModSum += 1 + (double)i/10;
			}
			if(autoGearSide.get(i)){
				sideSum +=1;
				sideModSum += 1 + (double)i/10;
			}
			matchsum += 1 + (double)i/10;
		}
		avgGearMod = gearModSum/matchsum;
		avgBallMod = ballModSum/matchsum;
		avgClimbMod = climbModSum/matchsum;
	}
	public String getTeamName() {
		return teamName;
	}
	public double getAvgBallMod(){
		return avgBallMod;
	}
	public int getTeamNum() {
		return teamNum;
	}
	public int getGearSum() {
		return gearSum;
	}
	public int getCenterSum() {
		return centerSum;
	}
	public int getSideSum(){
		return sideSum;
	}
	public double getAvgGear() {
		return avgGear;
	}
	public double getAvgGearMod() {
		return avgGearMod;
	}
	public double calcRank(double maxBall, double maxGear, double maxClimb){
		return (avgBallMod/maxBall) + (avgGearMod/maxGear)+ ((avgClimbMod*100)/maxClimb);
	}
	public double getClimbPerc(){
		return avgClimbMod * 100;
	}
}
