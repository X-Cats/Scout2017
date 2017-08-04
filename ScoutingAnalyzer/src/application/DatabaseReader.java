package application;

import java.sql.*;

public class DatabaseReader {
	private String query;
	private Connection db;
	public ResultSet rs;
	public DatabaseReader(Connection db){
		this.db = db;
	}
	public void executeQuery(){
		Statement out = null;
		try {
			out = db.createStatement();
			rs = out.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void setQuery(String newQuery){
		this.query = newQuery;
	}
	public int getSize(String table){
		int size = 0;
		Statement out = null;
		try{
			out = db.createStatement();
			ResultSet sizeRS = out.executeQuery("SELECT count(*) FROM " + Main.getCurrentSchema() + "." + table + ";");
			sizeRS.next();
			size=sizeRS.getInt(sizeRS.getRow());
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return size;
	}
}
