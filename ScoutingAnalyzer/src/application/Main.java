package application;
	
import java.sql.Connection;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	private static String currentSchema = "worlds"; //worlds for worlds schema
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("ScoutAnalyzer.fxml"));
			Scene scene = new Scene(root,1280,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Scouting Data Viewer");
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
