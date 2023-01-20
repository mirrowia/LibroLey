package com.mirrow.rotoplas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
	
    private Connection connection = null;
    private Statement statement = null;
	private ResultSet resultSet = null;
	
	//Getters and Setters
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	//Constructor
	public void dbConnection() {
        
	}
	
	public void startConnection() {
		// Step 1: Loading or registering Oracle JDBC driver class
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        }
        catch(ClassNotFoundException cnfex) {

            System.out.println("Problem in loading or registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }

        // Step 2: Opening database connection
        try {

            String msAccDB = "RotoPlasDatos.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 

            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL); 

        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
	}
	
	public void closeConnection() {
		// Step 3: Closing database connection
        try {
            if(null != connection) {

                // cleanup resources, once after processing
                resultSet.close();
                statement.close();

                // and then finally close connection
                connection.close();
            }
        }
        catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
	}
}
