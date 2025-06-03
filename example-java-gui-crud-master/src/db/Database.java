package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    
    // DATABASE CONNECTION URL POINTING TO A SQLITE FILE NAMED users.db
    private static final String URL = "jdbc:sqlite:users.db";

    // METHOD TO GET A CONNECTION OBJECT TO THE DATABASE
    public static Connection getConnection() throws SQLException {
        // RETURNS A NEW CONNECTION TO THE DATABASE USING THE URL
        return DriverManager.getConnection(URL);
    }
}
