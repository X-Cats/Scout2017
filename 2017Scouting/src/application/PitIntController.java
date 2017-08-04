package application;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


public class PitIntController {
	FileReader fr = null;
	FileChooser choose = new FileChooser();
	File csv;
	DatabaseWriter wr;
	public PitIntController(){
		wr = new DatabaseWriter(Main.db);
	}
	private void initialize(){
		csv=null;
	}
	@FXML
    private TextField teamNameField;

    @FXML
    private  TextField teamNumField;

    @FXML
    private  TextField gearNumField;

	@FXML
    private  CheckBox gearDoBox;

    @FXML
    private  RadioButton activePlace;

    @FXML
    private  ToggleGroup placementToggleGroup;

    @FXML
    private  RadioButton passivePlace;

    @FXML
    private  CheckBox acqFromPS;

    @FXML
    private  CheckBox acqFromGnd;

    @FXML
    private  CheckBox highGoal;

    @FXML
    private  TextField ballCap;

    @FXML
    private  CheckBox doBalls;

    @FXML
    private  CheckBox ballsAcqFromPS;

    @FXML
    private  CheckBox ballsAcqFromGnd;

    @FXML
    private  TextField climbTime;

    @FXML
    private  CheckBox climbDo;

    @FXML
    private  CheckBox centerAuto;

    @FXML
    private  CheckBox sideAuto;

    @FXML
    private  CheckBox gearAuto;

    @FXML
    private  TextField autoBallCap;

    @FXML
    private  CheckBox ballAuto;

    @FXML
    private  TextArea notesPage;

    @FXML
    private Button submitButton;
    
    @FXML
    private Text errText;
    
    @FXML
    private MenuItem quitItem;

    @FXML
    private MenuItem clearFieldItem;

    @FXML
    void clearFields(ActionEvent event) {
    	teamNameField.setText(null);
    	teamNumField.setText(null);
    	gearNumField.setText(null);
    	gearDoBox.setSelected(false);
    	acqFromPS.setSelected(false);
    	acqFromGnd.setSelected(false);
    	highGoal.setSelected(false);
    	ballCap.setText(null);
    	doBalls.setSelected(false);
    	ballsAcqFromPS.setSelected(false);
    	ballsAcqFromGnd.setSelected(false);
    	climbTime.setText(null);
    	climbDo.setSelected(false);
    	centerAuto.setSelected(false);
    	sideAuto.setSelected(false);
    	gearAuto.setSelected(false);
    	autoBallCap.setText(null);
    	ballAuto.setSelected(false);
    	notesPage.setText(null);
    }

    @FXML
    void exitApp(ActionEvent event) {
    	Platform.exit();
    	System.exit(0);
    }
    @FXML
    public void importData() throws IOException{
    	choose.setTitle("Select CSV File");
    	choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
    	String userDirectoryString = System.getProperty("user.home");
    	File userDirectory = new File(userDirectoryString);
    	if(!userDirectory.canRead()) {
    	    userDirectory = null;
    	}
    	choose.setInitialDirectory(userDirectory);
    	csv = choose.showOpenDialog(new Stage());
    	fr = new FileReader(csv);
    	Scanner fileIn = new Scanner(fr);
    	String notes = null;
    	
    	
    	while(fileIn.hasNextLine()){
    		String in = fileIn.nextLine();
    		String[] fields = in.split(",");
    		System.out.print(Arrays.toString(fields));
    		try{
        		notes = removeBadChars(fields[21]);
        	}
        	catch(ArrayIndexOutOfBoundsException e){
        		e.printStackTrace();
        		notes = " ";
        	}
    		String matchScoutQuery = "INSERT INTO " + Main.getCurrentSchema() + ".match_team_performance(TEAMNAME, TEAMNUM, MATCHNUM, ALLIANCE, AUTOBLCROSS, FUELHIGH,FUELLOW,KPA,"
    				+ "AUTOGEARLEFT, AUTOGEARLEFTFAILED, AUTOGEARRIGHT,AUTOGEARRIGHTFAILED,AUTOGEARCENTER,AUTOGEARCENTERFAILED,TELEOPGEARSPLACED,"
    				+ "TELEOPGEARSFTPLACE,TELEBALLS,CLIMB,FAILTOTOUCH,DNACLIMB,MALFUNCTION,NOTES) VALUES("
    				+ "'" + fields[0] +"'," + fields[1] + "," + fields[2] + ",'" + fields[3] + "'," + fields[4] + "," + fields[5] + "," + fields[6] + "," + parseInt(fields[7])
    				+ "," + fields[8] + "," +fields[9]+ ","+fields[10]+ ","+fields[11]+ ","+fields[12]+ ","+fields[13]+ ","+parseInt(fields[14])+ ","+parseInt(fields[15])
    				+ "," + parseInt(fields[16])+ ","+fields[17]+ ","+fields[18] + "," + fields[19]+ ","+fields[20] + ",'" + notes+ "');";
    		System.out.println(matchScoutQuery);
    		wr.setQuery(matchScoutQuery);
			wr.executeUpdate();
    	}
    }
    @FXML
	public void handleSubmitButtonClick(){		
		System.out.println("Submit!");
		if(getTeamNum() != 0){
			errText.setVisible(false);
			String pitScoutQuery = "INSERT INTO "+Main.getCurrentSchema()+".teams(TEAMNAME, TEAMNUM, GEARNUM, TELEGEAR, GEARPLACE,GEARACQPS,GEARACQGND,"
					+ "TELEBALLSHIGH,TELEBALLS,BALLSACQPS,BALLSACQGND,CLIMBTIME,CLIMB,CLIMBINF,"
					+ "AUTOGEARPLACESIDE,AUTOGEARPLACECENTER,AUTOGEAR,AUTOBALLCAP,AUTOBALLS,NOTES) "
					+ "VALUES ("
					+ "'" + getTeamName() + "'," + getTeamNum() + ","
					+ getGearNum() + "," + getGearDo() + ","
					+ getPlacementToggle() + "," + getAcqFromPS() + "," + getAcqFromGnd() + ","
					+ getHighGoal() + "," + getDoBalls() + "," + getBallsAcqFromPS() + ","
					+ getBallsAcqFromGnd() + "," + getClimbTime() + "," + getClimbDo()+",'  ',"
					+ getSideAuto() + "," + getCenterAuto() + "," + getGearAuto() + ","
					+ getAutoBallCap() +"," + getBallAuto() + ",'" + removeBadChars(getNotesPage()) + "'"
					+ ");";
			
			System.out.println(pitScoutQuery);
			wr.setQuery(pitScoutQuery);
			wr.executeUpdate();
		}
		else{
			errText.setVisible(true);
		}
	}

	public  String getTeamName() {
		return teamNameField.getText();
	}

	public  int getTeamNum() {
		if(teamNumField.getText() == null) return 0;
		else{
			try{
				return Integer.parseInt(teamNumField.getText());
			}
			catch(NumberFormatException n){
				System.out.println("Not Valid!");
				return 0;
			}
		}
	}

	public int getGearNum() {
		if(gearNumField.getText() == null) return 0;
		else{
			try{
				return Integer.parseInt(gearNumField.getText());
			}
			catch(NumberFormatException n){
				System.out.println("Not Valid!");
				return 0;
			}
		}
	}

	public  Boolean getGearDo() {
		return gearDoBox.isSelected();
	}

	public  boolean getPlacementToggle() {
		Toggle in = placementToggleGroup.getSelectedToggle();
		boolean out = false;
		if(in == activePlace) out = true;
		
		return out;
	}

	public  boolean getAcqFromPS() {
		return acqFromPS.isSelected();
	}

	public  boolean getAcqFromGnd() {
		return acqFromGnd.isSelected();
	}

	public  boolean getHighGoal() {
		return highGoal.isSelected();
	}

	public  int getBallCap() {
		if(ballCap.getText() == null) return 0;
		else{
			try{
				return Integer.parseInt(ballCap.getText());
			}
			catch(NumberFormatException n){
				System.out.println("Not Valid!");
				return 0;
			}
		}
	}

	public  boolean getDoBalls() {
		return doBalls.isSelected();
	}

	public  boolean getBallsAcqFromPS() {
		return ballsAcqFromPS.isSelected();
	}

	public  boolean getBallsAcqFromGnd() {
		return ballsAcqFromGnd.isSelected();
	}

	public  int getClimbTime() {
		if(climbTime.getText() == null) return 1000;
		else{
			try{
				return Integer.parseInt(climbTime.getText());
			}
			catch(NumberFormatException n){
				System.out.println("Not Valid!");
				return 1000;
			}
		}
	}

	public  boolean getClimbDo() {
		return climbDo.isSelected();
	}

	public  boolean getCenterAuto() {
		return centerAuto.isSelected();
	}

	public  boolean getSideAuto() {
		return sideAuto.isSelected();
	}

	public  boolean getGearAuto() {
		return gearAuto.isSelected();
	}

	public  int getAutoBallCap() {
		if(autoBallCap.getText() == null) return 0;
		else{
			try{
			return Integer.parseInt(autoBallCap.getText());
			}catch(NumberFormatException n){
				System.out.println("Not Valid!");
				return 0;
			}
		}
	}

	public boolean getBallAuto() {
		return ballAuto.isSelected();
	}

	public  String getNotesPage() {
		return notesPage.getText();
	}
	public String removeBadChars(String input) {
		char[] inputChar;
		try{
			inputChar = input.toCharArray();
		}
		catch(NullPointerException e){
			e.printStackTrace();
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (char i : inputChar) {
			if (i == '\'' || i == ';')
				sb.append("");
			else
				sb.append(i);
		}

		return sb.toString();
	}
	public int parseInt(String in){
		int out = 0;
		if(!in.equals("") || !in.equals(" ")){
    		try{
    			out =  Integer.parseInt(in);
    			return out;
    		}
    		catch(NumberFormatException n){
    			n.printStackTrace();
    			out = 0;
    			return out;
    		}
		}
		else{
			return 0;
		}
	}
}
