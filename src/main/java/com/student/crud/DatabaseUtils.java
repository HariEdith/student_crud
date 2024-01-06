package com.student.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/student_database";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Hari2002haran@h2";
    private static Connection connection;

    private DatabaseUtils() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
    	try {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }}
    	catch(SQLException e ) {
    		System.out.println("error in close connection");
    		e.printStackTrace();
    	}
    }

   }
