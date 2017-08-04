package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.controlsfx.control.textfield.TextFields;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class ScoutAnalyzerController {
	//Data for graphs
	XYChart.Series gearRaw = new XYChart.Series();
	XYChart.Series ballRaw = new XYChart.Series();

	//Modified data
	ArrayList<Double> gearMod = new ArrayList<Double>();
	ArrayList<Double> gearDropMod = new ArrayList<Double>();
	
	//Data for the table
	CheckBoxTreeItem<String> pitData = new CheckBoxTreeItem<String>("Pit Data");
	CheckBoxTreeItem<String> rawMatchData = new CheckBoxTreeItem<String>("Match Data");
	CheckBoxTreeItem<String> rawData = new CheckBoxTreeItem<String>("Raw Data");
	
	//Data for the table options list
	List<CheckBoxTreeItem<String>> pitFields = new ArrayList<CheckBoxTreeItem<String>>();
	List<CheckBoxTreeItem<String>> rawMatchFields = new ArrayList<CheckBoxTreeItem<String>>();
    Set<TreeItem<String>> selected = new HashSet<>();
    
    //Data for Rankings
    ArrayList<TeamPerf> teamPerf = new ArrayList<>();
    ArrayList<String> teamNames = new ArrayList<>();
    
    //Database reader.
	DatabaseReader dbread = new DatabaseReader(Main.db);
	
	public void initialize(){
		//Generate List of teams from db
		genTeamList();
		
		//Generate table options
		genTreeItems();
		
		//Settings for gear graph
		gpmGraphX.setAutoRanging(false);
		gpmGraphX.setLowerBound(1.0);
		gpmGraphX.setTickUnit(1.0);
		gpmGraphY.setAutoRanging(true);
		
		//Settings for ball graph
		bpmGraphX.setAutoRanging(false);
		bpmGraphX.setLowerBound(1.0);
		bpmGraphX.setTickUnit(1.0);
		bpmGraphY.setAutoRanging(true);
		
		//Set format for table options
		tableOptionsTreeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());

	}
	private int matchNotesNum;
	private String matchData = "";
	
	@FXML
	private TextArea PitNotesField;
	
	@FXML
	private TextArea MatchNotesField;
	
	@FXML
	private Text EntriesText;
	
	@FXML
    private LineChart<Number, Number> gpmGraph;
	
	@FXML
    private LineChart<Number, Number> bpmGraph;

    @FXML
    private NumberAxis gpmGraphX;

    @FXML
    private NumberAxis gpmGraphY;
    
    @FXML
    private NumberAxis bpmGraphX;

    @FXML
    private NumberAxis bpmGraphY;

    @FXML
    private TableView<MatchTeamPerf> teamInfoTable;

    @FXML
    private TreeView<String> tableOptionsTreeView;
    
    @FXML
    private TreeView<?> rankTableOptions;

    @FXML
    private TableView<TeamPerf> rankTable;

    @FXML
    private ComboBox teamMenu;

    @FXML
    private Text gearsDroppedText;

    @FXML
    private Text gearsPlacedText;
    
    @FXML
    private Text climbRate;
    
    @FXML
    private void refresh(){
    	genTeamList();
    }
    
    public void genTreeItems(){
    	
    	String rawPitFields[] = {
				"Team Name", "Team Number"
		};
    	String MatchFields[] = {
				"Match Number","Gears Scored", "Balls Scored (kPa)"
		};
		
		
		for(String i : rawPitFields){
			CheckBoxTreeItem<String> newItem = new CheckBoxTreeItem<String>(i);
			pitFields.add(newItem);
		}
		for(String i : MatchFields){
			CheckBoxTreeItem<String> newItem = new CheckBoxTreeItem<String>(i);
			rawMatchFields.add(newItem);
		}
		rawData.setExpanded(true);
		rawData.getChildren().add(pitData);
		rawData.getChildren().add(rawMatchData);
		pitData.getChildren().addAll(pitFields);
		pitData.setSelected(true);
		rawMatchData.getChildren().addAll(rawMatchFields);
		
		tableOptionsTreeView.setRoot(rawData);
    }
    public void genTeamList(){
    	teamMenu.getItems().clear();
    	int resultSize = dbread.getSize("teams");
    	dbread.setQuery("SELECT teamnum,teamname FROM " + Main.getCurrentSchema() +".teams ORDER BY teamnum;");
    	dbread.executeQuery();
    	System.out.println(resultSize);
    	teamNames = new ArrayList<String>();
    	String newItem = null;
    	for(int i = 1; i <= resultSize; i++){
    		try {
    			dbread.rs.next();
				newItem = dbread.rs.getString(1) + " - " + dbread.rs.getString(2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("FAILED TO ADD ELEMENT");
				e.printStackTrace();
			}
    		teamNames.add(newItem);
    	}
    	teamMenu.getItems().addAll(teamNames);
    	teamMenu.setEditable(true);
    	TextFields.bindAutoCompletion(teamMenu.getEditor(), teamMenu.getItems());
    }
    @FXML
    public void updateInfo(){
    	double matchsum = 0;
    	double climbTotal = 0;
    	double climbPercent = 0;
    	gearRaw.getData().removeAll();
    	ballRaw.getData().removeAll();
    	gearMod = new ArrayList<Double>();
    	gearDropMod = new ArrayList<Double>();
    	gpmGraph.getData().removeAll();
    	bpmGraph.getData().removeAll();
    	double gearPlacedSum = 0;
    	double gearDroppedSum = 0;
    	String gpAvg = null;
    	String gdAvg = null;
    	String newTeamNum=teamMenu.getValue().toString().substring(0,teamMenu.getValue().toString().indexOf(" "));
    	System.out.println(newTeamNum);
    	dbread.setQuery("SELECT teleopgearsplaced, teleopgearsftplace, teleballs, climb, notes, matchnum FROM " + Main.getCurrentSchema() + ".match_team_performance WHERE teamnum = " + newTeamNum + "ORDER by matchnum;");
    	dbread.executeQuery();
    	matchData = "";
    	for(int i = 0; i < 10; i++){
    		try {
				dbread.rs.next();
				int currGearNum = dbread.rs.getInt(1);
				int currGearDrop = dbread.rs.getInt(2);
				gearRaw.getData().add(new XYChart.Data(i+1, currGearNum ));
				ballRaw.getData().add(new XYChart.Data(i+1, dbread.rs.getInt(3)));
				matchData += "Match " + dbread.rs.getString(6) + ": " + dbread.rs.getString(5) + "\n";
				matchNotesNum++;
				
				gearMod.add((double)currGearNum * (1.0 + (((double)i)/10.0)));
				gearDropMod.add((double)currGearDrop * (1.0 + (((double)i)/10.0)));
				if(dbread.rs.getBoolean(4)){
					climbTotal += 1 + i/10;
				}
				matchsum += 1 + i/10;
			} catch (SQLException e) {
				System.out.println("No data for match " + (i+1) + " for team number " + newTeamNum);
			}
    	}
    	for(double d : gearMod){
    		gearPlacedSum+=d;
    	}
    	for(double d : gearDropMod){
    		gearDroppedSum += d;
    	}
    	gpAvg = String.format("%.2f", (gearPlacedSum/matchsum));
    	climbPercent = (climbTotal/(double) matchsum)*100.0;
    	gdAvg = String.format("%.2f", (gearDroppedSum/matchsum));
    	gearsPlacedText.setText(gpAvg);
    	gearsDroppedText.setText(gdAvg);
    	EntriesText.setText(matchNotesNum + " Entries");
    	dbread.setQuery("SELECT notes FROM " + Main.getCurrentSchema() + ".teams WHERE teamnum = " + newTeamNum + ";");
    	dbread.executeQuery();
    	try {
    		dbread.rs.next();
			PitNotesField.setText(dbread.rs.getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PitNotesField.setText("Error");
		}
    	MatchNotesField.setText(matchData);
		climbRate.setFill(Color.BLACK);
    	if(climbPercent < 60.0){
    		climbRate.setFill(Color.RED);
    	}
    	climbRate.setText(String.format(" %3.0f%%", climbPercent));
    	gearRaw.setName(newTeamNum + " - Gears per Match");
    	ballRaw.setName(newTeamNum + " - Balls per Match");
    	gpmGraph.getData().add(gearRaw);
    	bpmGraph.getData().add(ballRaw);
    }
    
    @SuppressWarnings("unchecked")
	@FXML
    public void updateTable(){
    	
    	ArrayList<MatchTeamPerf> data = new ArrayList<>();
    	
    	String dataQuery = "SELECT matchnum,teamname,teamnum,teleopgearsplaced,teleballs FROM " + Main.getCurrentSchema() + ".match_team_performance ORDER BY matchnum;";
    	TableColumn<MatchTeamPerf,Number> matchNumCol = new TableColumn<>("Match #");
    	TableColumn<MatchTeamPerf, String> nameCol = new TableColumn<>("Team Name");
    	TableColumn<MatchTeamPerf, Number> numCol = new TableColumn<>("Team #");
    	TableColumn<MatchTeamPerf,Number> gearsScoredCol = new TableColumn<>("Gears Scored");
    	TableColumn<MatchTeamPerf,Number> ballsScoredCol = new TableColumn<>("Balls Scored (kPa)");

    	
    	ArrayList<Integer> matchNums = new ArrayList<>();
    	ArrayList<String> teamNames = new ArrayList<>();
    	ArrayList<Integer> teamNums = new ArrayList<>();
    	ArrayList<Integer> gearsPlaced = new ArrayList<>();
    	ArrayList<Integer> ballsScored = new ArrayList<>();

    	matchNumCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getMatchNumber()));
    	nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeamName()));
    	numCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTeamNumber()));
    	gearsScoredCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getGearsScored()));
    	ballsScoredCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getBallsScored()));

    	dbread.setQuery(dataQuery);
    	dbread.executeQuery();
    	ResultSet RS = dbread.rs;
    	try {
			while(RS.next()){
				matchNums.add(RS.getInt(1));
				teamNames.add(RS.getString(2));
				teamNums.add(RS.getInt(3));
				gearsPlaced.add(RS.getInt(4));
				ballsScored.add(RS.getInt(5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//Remove old data to avoid duplicates
    	teamInfoTable.getColumns().removeAll(teamInfoTable.getColumns());
    	teamInfoTable.getItems().removeAll(teamInfoTable.getItems());
    	
    	//Gather data for table
    	for(int i = 0; i < matchNums.size(); i++){
    		data.add(new MatchTeamPerf(matchNums.get(i), teamNames.get(i), teamNums.get(i),gearsPlaced.get(i),ballsScored.get(i)));
    	}
    	
    	//Add data to the table
    	teamInfoTable.getItems().addAll(data);
    	
    	//Add columns to the table depending on what is selected in the options tree
    	if(rawMatchFields.get(0).isSelected()){
    		teamInfoTable.getColumns().add(matchNumCol);
    	}
    	if(rawMatchFields.get(1).isSelected()){
    		teamInfoTable.getColumns().add(gearsScoredCol);
    	}
    	if(rawMatchFields.get(2).isSelected()){
    		teamInfoTable.getColumns().add(ballsScoredCol);
    	}
    	if(pitFields.get(0).isSelected()){
    		teamInfoTable.getColumns().add(nameCol);
    	}
    	if(pitFields.get(1).isSelected()){
    		teamInfoTable.getColumns().add(numCol);
    	}
    }
    @FXML
    public void updateRankTable(){
    	double oldMaxBall = 0;
    	double oldMaxClimb = 0;
    	double oldMaxGear = 0;
    	genTeamList();
    	TableColumn<TeamPerf, Number> placeCol = new TableColumn<>("Place");
    	TableColumn<TeamPerf, Number> numCol = new TableColumn<>("Team #");
    	TableColumn<TeamPerf, String> nameCol = new TableColumn<>("Team Name");
    	TableColumn<TeamPerf, Number> gearModCol = new TableColumn<>("Avg. # of Gears (Mod.)");
    	TableColumn<TeamPerf, Number> gearCenterCol = new TableColumn<>("Auto Gear in Center");
    	TableColumn<TeamPerf, Number> gearSideCol = new TableColumn<>("Auto Gear on Side");
    	TableColumn<TeamPerf, Number> ballModCol = new TableColumn<>("Avg. # of Balls (Mod.)");
    	TableColumn<TeamPerf, Number> gearTotalCol = new TableColumn<>("Total # of Gears Placed");
    	TableColumn<TeamPerf, Number> climbPercentage = new TableColumn<>("Climb Percentage");
    	TableColumn<TeamPerf, Number> rankCol = new TableColumn<>("Raw Rank");

    	for(int i = 0; i < teamNames.size(); i++){
    		String teamNum = teamNames.get(i).substring(0, teamNames.get(i).indexOf(" "));
    		ArrayList<Integer> matches = new ArrayList<>();
    		ArrayList<Integer> gears = new ArrayList<>();
    		ArrayList<Integer> balls = new ArrayList<>();
    		ArrayList<Boolean> climb = new ArrayList<>();
    		ArrayList<Boolean> autoSide = new ArrayList<>();
    		ArrayList<Boolean> autoCenter = new ArrayList<>();
    		String dataQuery = "SELECT matchnum,teleopgearsplaced,teleballs,climb,autogearleft,autogearright,autogearcenter FROM " + Main.getCurrentSchema() + ".match_team_performance WHERE teamnum = " + teamNum + " ORDER BY matchnum;";
    		dbread.setQuery(dataQuery);
        	dbread.executeQuery();
        	ResultSet RS = dbread.rs;
        	try {
				while(RS.next()){
					matches.add(RS.getInt(1));
					gears.add(RS.getInt(2));
					balls.add(RS.getInt(3));
					climb.add(RS.getBoolean(4));
					autoSide.add(RS.getBoolean(5) || RS.getBoolean(6));
					autoCenter.add(RS.getBoolean(7));
					System.out.println(autoCenter);
				}
				TeamPerf newTeamPerf = new TeamPerf(Integer.parseInt(teamNum), teamNames.get(i).substring(teamNames.get(i).lastIndexOf("- ")+2), gears, matches,balls,climb,autoCenter, autoSide, 1);
				if(newTeamPerf.getClimbPerc() > oldMaxClimb) oldMaxClimb = newTeamPerf.getClimbPerc();
				if(newTeamPerf.getAvgGearMod() > oldMaxGear) oldMaxGear = newTeamPerf.getAvgGearMod();
				if(newTeamPerf.getAvgBallMod() > oldMaxBall) oldMaxBall = newTeamPerf.getAvgBallMod();
				teamPerf.add(newTeamPerf);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	placeCol.setSortable(false);
    	
    	final double maxBall = oldMaxBall;
    	System.out.println(oldMaxBall);
    	final double maxGear = oldMaxGear;
    	System.out.println(maxGear);
    	final double maxClimb = oldMaxClimb;
    	System.out.println(maxClimb);
    	placeCol.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(1 + rankTable.getItems().indexOf(column.getValue())));
    	numCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTeamNum()));
    	nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeamName()));
    	ballModCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getAvgBallMod()));
    	gearModCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getAvgGearMod()));
    	gearTotalCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getGearSum()));
    	rankCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().calcRank(maxBall,maxGear,maxClimb)*100.0));
    	climbPercentage.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getClimbPerc()));
    	gearCenterCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getCenterSum()));
    	gearSideCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getSideSum()));
    	
    	//Remove old data to avoid duplicates
    	rankTable.getColumns().removeAll(rankTable.getColumns());
    	rankTable.getItems().clear();
    	//Now add the new data
    	rankTable.getItems().addAll(teamPerf);
    	
    	//Finally add the rows. TODO Make this effected by the treeview on the side. Currently very quick and dirty.
    	rankTable.getColumns().addAll(placeCol,numCol,nameCol,gearModCol,gearTotalCol,rankCol,climbPercentage,ballModCol,gearCenterCol,gearSideCol);
    }
}
