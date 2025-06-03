package dal.admins;

import db.Database;

import java.sql.*;

public class AdminDAO {
    
    // CONSTRUCTOR - CALLED WHEN AN INSTANCE OF AdminDAO IS CREATED
    public AdminDAO() {
        createTable();  // ENSURES THE ADMIN TABLE EXISTS
    }

    // CREATES THE ADMINS TABLE IF IT DOES NOT ALREADY EXIST
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS admins (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)";
        try (Connection conn = Database.getConnection();  // ESTABLISHES A DATABASE CONNECTION
             Statement stmt = conn.createStatement()) {   // CREATES A STATEMENT TO EXECUTE SQL
            stmt.execute(sql);                            // EXECUTES THE SQL STATEMENT TO CREATE THE TABLE
        } catch (SQLException e) {
            e.printStackTrace();                          // PRINTS THE STACK TRACE IF AN SQL EXCEPTION OCCURS
        }
    }

    // CHECKS IF AN ADMIN ACCOUNT EXISTS IN THE DATABASE WITH GIVEN USERNAME AND PASSWORD
    public boolean checkIfAdminExists(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (
                Connection conn = Database.getConnection();           // OPENS A CONNECTION
                PreparedStatement pstmt = conn.prepareStatement(sql)  // PREPARES THE SQL QUERY
        ) {
            pstmt.setString(1, username);                             // SETS THE USERNAME PARAMETER
            pstmt.setString(2, password);                             // SETS THE PASSWORD PARAMETER

            try (ResultSet rs = pstmt.executeQuery()) {               // EXECUTES QUERY AND GETS RESULTS
                return rs.next();                                     // RETURNS TRUE IF A MATCHING RECORD IS FOUND
            }
        } catch (SQLException e) {
            e.printStackTrace();                                      // HANDLES SQL EXCEPTIONS
        }
        return false;  // RETURNS FALSE IF NO MATCHING RECORD IS FOUND OR EXCEPTION OCCURS
    }

    // ADDS A NEW ADMIN ACCOUNT TO THE DATABASE
    public void addSignupAccount(String username, String password){
        String sql = "INSERT INTO admins(username, password) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();              // OPENS A CONNECTION TO THE DATABASE
             PreparedStatement pstmt = conn.prepareStatement(sql)) {  // PREPARES INSERT STATEMENT
            pstmt.setString(1, username);                             // SETS THE USERNAME PARAMETER
            pstmt.setString(2, password);                             // SETS THE PASSWORD PARAMETER
            pstmt.executeUpdate();                                    // EXECUTES THE INSERT STATEMENT
        } catch (SQLException e) {
            e.printStackTrace();                                      // PRINTS THE STACK TRACE IN CASE OF EXCEPTION
        }
    }
}
