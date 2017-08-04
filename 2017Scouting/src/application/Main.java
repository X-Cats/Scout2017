package application;
	
import java.sql.*;
import java.util.Properties;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;


public class Main extends Application {
	private static String currentSchema = "iri";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane pitInt = (BorderPane)FXMLLoader.load(getClass().getResource("PitInterface.fxml"));
			Scene scene = new Scene(pitInt,1280,800);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection db;
	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		String url = "jdbc:postgresql://localhost/Scout2017";
		DBConnection scoutDB = new DBConnection(url,"alfs","foo");
		Connection database = scoutDB.connect();
		Main.db = database;
		launch(args);
	}
	public static String getCurrentSchema(){
		return currentSchema;
	}
}
