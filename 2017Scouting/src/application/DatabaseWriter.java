package application;

import java.sql.*;

public class DatabaseWriter {
	private String query;
	private Connection db;
	public DatabaseWriter(Connection db){
		this.db = db;
	}
	public void executeUpdate(){
		Statement out = null;
		try {
			out = db.createStatement();
			out.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void setQuery(String newQuery){
		this.query = newQuery;
	}
}
