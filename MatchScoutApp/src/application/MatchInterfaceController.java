package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class MatchInterfaceController {
	static FileWriter fw = null;
	FileChooser choose = new FileChooser();
	File csv;
	private String lastSave = null;
	public MatchInterfaceController() {
	}

	private void initialize() {
		csv = null;
		this.clearFields();
	}

	private SpinnerValueFactory<Integer> gearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,0);

	@FXML
	private TextField teamNameField;

	@FXML
	private TextField teamNumField;

	@FXML
	private TextField matchNumField;
	
	@FXML
	private RadioButton redAlliance;

	@FXML
	private ToggleGroup allTg;

	@FXML
	private RadioButton blueAlliance;

	@FXML
	private CheckBox xBaseLine;

	@FXML
	private CheckBox autoHighFuel;

	@FXML
	private CheckBox autoLowFuel;

	@FXML
	private TextField autokPa;

	@FXML
	private CheckBox autoLeftGear;

	@FXML
	private CheckBox autoLeftGearFailed;

	@FXML
	private CheckBox autoCenterGear;

	@FXML
	private CheckBox autoCenterGearFailed;

	@FXML
	private CheckBox autoRightGear;

	@FXML
	private CheckBox autoRightGearFailed;

	@FXML
	public Spinner gearPlacedSpin;

	@FXML
	public Spinner gearFTPlaceSpin;

	@FXML
	public Spinner ballScoredSpin;

	@FXML
	private CheckBox climbed;

	@FXML
	private CheckBox daClimb;
	
	@FXML
	private CheckBox failedToTouch;

	@FXML
	private CheckBox malfunction;

	@FXML
	private TextArea notesField;

	@FXML
	private MenuItem fileCloseItem;

	@FXML
	private MenuItem fileSaveItem;

	@FXML
	private MenuItem fileSaveAsItem;
	
	@FXML
	private MenuItem fileChangeFileItem;

	@FXML
	private MenuItem helpAboutItem;

	@FXML
	public void closeWindow() {
		Main.csvfiler.closeFiles();
		Platform.exit();
		System.exit(0);
	}
	

	@FXML
	void openAboutMenu(ActionEvent event) {

	}

	@FXML
	void saveFile() {
		if(Main.csvfiler.getOutFile() == null) {
			changeFile();
		}else {
			Main.csvfiler.saveFile(DataHandler.allToCSV());
		}
	}
	@FXML
	void changeFile() {
		choose.setTitle("Save CSV File");
		choose.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		// Set to user directory or go to default if cannot access
		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);
		if (!userDirectory.canRead()) {
			userDirectory = null;
		}
		choose.setInitialDirectory(userDirectory);
		File temp = choose.showSaveDialog(new Stage());
		Main.csvfiler.createWriter(temp);
		Main.csvfiler.saveFile(DataHandler.allToCSV());
	}

	@FXML
	void saveData() {
		DataHandler.addData(this.getTeamName(),this.getTeamNum(),
				this.getMatchNum(), this.getAlliance(),
				this.getBaselineCrossed(), this.getAutoHighFuel(),
				this.getAutoLowFuel(), this.getAutokPa(), 
				this.getAutoLeftGear(), this.getAutoLeftGearFailed(),
				this.getAutoCenterGear(), this.getAutoCenterGearFailed(),
				this.getAutoRightGear(), this.getAutoRightGearFailed(),
				this.getGearsPlaced(), this.getGearsFailed(),
				this.getKPaScored(), this.getClimb(), 
				this.getClimbFailedToTouch(), this.getClimbNotAttempted(),
				this.getClimbMalfunction(), this.getNotes());
	}

	@FXML
	private void clearFields() {
		teamNameField.setText(null);
		teamNumField.setText(null);
		matchNumField.setText(null);

		xBaseLine.setSelected(false);
		autoHighFuel.setSelected(false);
		autoLowFuel.setSelected(false);
		autokPa.setText(null);
		autoLeftGear.setSelected(false);
		autoLeftGearFailed.setSelected(false);
		autoRightGear.setSelected(false);
		autoRightGearFailed.setSelected(false);
		autoCenterGear.setSelected(false);
		autoCenterGearFailed.setSelected(false);
		gearPlacedSpin.getEditor().setText("0");
		gearFTPlaceSpin.getEditor().setText("0");
		ballScoredSpin.getEditor().setText("0");
		climbed.setSelected(false);
		failedToTouch.setSelected(false);
		daClimb.setSelected(false);
		malfunction.setSelected(false);
		notesField.setText(null);
	}
	public String getTeamName() {
		try {
			if(teamNameField.getText()!=null)
				return teamNameField.getText();
			else
				return " ";
		}catch(NullPointerException e) {
			return " ";
		}
	}
	public int getTeamNum() {
		return Integer.parseInt(teamNumField.getText());
	}
	public int getMatchNum() {
		return Integer.parseInt(matchNumField.getText());
	}
	public boolean getAlliance() {
		if (allTg.getSelectedToggle() == redAlliance)
			return true;
		else
			return false;
	}
	public boolean getBaselineCrossed() {
		return xBaseLine.isSelected();
	}
	public boolean getAutoHighFuel() {
		return autoHighFuel.isSelected();
	}
	public boolean getAutoLowFuel() {
		return autoLowFuel.isSelected();
	}
	public int getAutokPa() {
		if (autokPa.getText() != null && autokPa.getText() != "")
			try { 
				return Integer.parseInt(autokPa.getText());
			}
			catch (NumberFormatException e) {
				System.err.println("\nkPa: No number found! Returning 0");
				return 0;
			}
		else
			return 0;
	}
	public boolean getAutoLeftGear() {
		return autoLeftGear.isSelected();
	}
	public boolean getAutoLeftGearFailed() {
		return autoLeftGearFailed.isSelected();
	}
	public boolean getAutoCenterGear() {
		return autoCenterGear.isSelected();
	}
	public boolean getAutoCenterGearFailed() {
		return autoCenterGearFailed.isSelected();
	}
	public boolean getAutoRightGear() {
		return autoRightGear.isSelected();
	}
	public boolean getAutoRightGearFailed() {
		return autoRightGearFailed.isSelected();
	}
	public int getGearsPlaced() {
		return Integer.parseInt(gearPlacedSpin.getEditor().getText());
	}
	public int getGearsFailed() {
		return Integer.parseInt(gearFTPlaceSpin.getEditor().getText());
	}
	public int getKPaScored() {
		return Integer.parseInt(ballScoredSpin.getEditor().getText());
	}
	public boolean getClimb() {
		return climbed.isSelected();
	}
	public boolean getClimbFailedToTouch() {
		return failedToTouch.isSelected();
	}
	public boolean getClimbNotAttempted() {
		return daClimb.isSelected();
	}
	public boolean getClimbMalfunction() {
		return malfunction.isSelected();
	}
	public String getNotes() {
		try {
			return notesField.getText();
		}catch(NullPointerException e) {
			e.printStackTrace();
			System.err.print("No notes. Returning none.");
			return "none";
		}
	}

	

}