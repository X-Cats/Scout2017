package application;

import java.sql.*;
public class DBConnection {
	private String dbURL;
	private String uName;
	private String uPass;
	public DBConnection(String url, String user, String password){
		dbURL=url;
		uName=user;
		uPass=password;
	}
	public Connection connect(){
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(dbURL,uName,uPass);
			System.out.println("Connection Successful!");
		}catch(SQLException e){
			System.out.println(e.getMessage());
			System.out.println("FAILED TO CONNECT");
		}
		
		return conn;
	}
}
