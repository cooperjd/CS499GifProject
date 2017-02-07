package database;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class Database {
	private static Database instance = null;
	private static String userName;
	//private static String password;
	private static Connection connection;
	
        //connects to the database
	public Database(){
		connect();
		try {
			if(connection != null || !connection.isClosed()){
				close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
        //gets an instance of the database
	public static Database getInstance(){
		if(instance == null){
			instance = new Database();
		}
		
		return instance;
	}
	
        //returns all student data i.e. name, username, password, and links to images
	public String[][] getStudentData(){
		String[][] studentData = new String[0][0];
		int rowCount = 0;
		int columnCount = 10;
		
		ResultSet result = query("select * from students");
		try {
			if(result.last()){
				rowCount = result.getRow();
				studentData = new String[rowCount][columnCount];
				
				result.beforeFirst();
				
				while(result.next()){
					int row = result.getRow();
					for(int i = 0; i < columnCount; i++){
						switch (i){
						case 0:
							studentData[row - 1][i] = result.getString("idstudents");
							break;
						case 1:
							studentData[row - 1][i] = result.getString("firstName");
							break;
						case 2:
							studentData[row - 1][i] = result.getString("lastName");
							break;
						case 3:
							studentData[row - 1][i] = result.getString("username");
							break;
						case 4:
							studentData[row - 1][i] = result.getString("password");
							break;
						case 5:
							studentData[row - 1][i] = result.getString("gifPath");
							break;
						}
					}
					}
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
		return studentData;
	}
	
        //returns the result of a query i.e. get the username of a particular student
	private ResultSet query(String myQuery){
		connect();
		ResultSet result = null;
		
		try{
			java.sql.Statement myStmt = connection.createStatement();
			
			result = myStmt.executeQuery(myQuery);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
                close();
		return result;
	}
	
        //checks if the username and password of a student matches the username and password in the database
	public boolean testCreds(String userName, String password){
		connect();
		boolean incorrectPass = false; 

		try{
			java.sql.Statement myStmt = connection.createStatement();
			
			ResultSet result = myStmt.executeQuery("select * from students");
			
			while(result.next()){
				if(result.getString("username").equals(userName) && result.getString("password").equals(password)){
					setCreds(userName, password);
					incorrectPass = false;
					System.out.println("Correct credentials");
					break;
				}else{
					incorrectPass = true;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		close();
		return incorrectPass;
	}
	
	//Gets the name of the user that is trying to log in.  User after the credentials have been retrieved.
	public String getName(){
		String name = "user";
		connect();
		
		try {
			java.sql.Statement myStmt = connection.createStatement();
			
			ResultSet result = myStmt.executeQuery("select * from students where username='" + userName + "'");
			
			result.next();
			name = result.getString("firstName") + " " + result.getString("lastName");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
		return name;
	}
	
	private void setCreds(String uname, String pw){
		userName = uname;
		//password = pw;
	}
	
        //sets the path of the gif file
        public void setPath(String path, String username){
            try{
            connect(); 
            String query = "UPDATE " + Constants.CONNECTION_DATABASE_NAME + "." + Constants.CONNECTION_TABLE_NAME + " SET gifPath = ? WHERE username = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, path);
            preparedStmt.setString(2, username);
            preparedStmt.executeUpdate();
            close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        //connects to the database using the defined constants in Constants class
	private void connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + InetAddress.getLocalHost().getHostAddress() + ":3306/" + Constants.CONNECTION_DATABASE_NAME, Constants.CONNECTION_USERNAME, Constants.CONNECTION_PASSWORD);
			System.out.println("Established Connection");
			

		}catch(SQLException e) {
			
			e.printStackTrace();
		}catch(ClassNotFoundException e){
                    e.printStackTrace();
                }catch(UnknownHostException e){
                    e.printStackTrace();
                }
	}
	
        //closes the connection to the database
	private void close(){
		try {
			connection.close();
			System.out.println("Connection Closed.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

        //cleans up the connection to the database
	public void cleanUpConnection() {
		System.out.println("Cleaning up connection...");
		try {
			if(!connection.isClosed()){
				close();
			}
			
			System.out.println("Cleaned up connection.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
