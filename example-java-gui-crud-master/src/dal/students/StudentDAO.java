package dal.students;

import db.Database;
import models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    // CONSTRUCTOR - CREATES TABLE ON OBJECT INSTANTIATION
    public StudentDAO() {
        createTable();
    }

    // CREATES THE STUDENTS TABLE IF IT DOES NOT ALREADY EXIST
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_number TEXT NOT NULL," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "program TEXT NOT NULL," +
                "level INTEGER NOT NULL)";
        try (Connection conn = Database.getConnection();  // OPENS DATABASE CONNECTION
             Statement stmt = conn.createStatement()) {   // CREATES A STATEMENT TO EXECUTE SQL
            stmt.execute(sql);                            // EXECUTES SQL TO CREATE TABLE
        } catch (SQLException e) {
            e.printStackTrace();                          // HANDLES SQL EXCEPTIONS
        }
    }

    // ADDS A NEW STUDENT RECORD TO THE DATABASE
    public void addStudent(Student student) {
        String sql = "INSERT INTO students(student_number, first_name, last_name, program, level) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();              // OPENS DATABASE CONNECTION
             PreparedStatement pstmt = conn.prepareStatement(sql)) {  // PREPARES SQL INSERT
            pstmt.setString(1, student.getStudentNumber());           // SETS STUDENT NUMBER
            pstmt.setString(2, student.getFirstName());               // SETS FIRST NAME
            pstmt.setString(3, student.getLastName());                // SETS LAST NAME
            pstmt.setString(4, student.getProgram());                 // SETS PROGRAM
            pstmt.setInt(5, student.getLevel());                      // SETS LEVEL
            pstmt.executeUpdate();                                    // EXECUTES THE INSERT STATEMENT
        } catch (SQLException e) {
            e.printStackTrace();                                      // HANDLES EXCEPTIONS
        }
    }

    // RETRIEVES ALL STUDENTS FROM THE DATABASE
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = Database.getConnection();  // OPENS DATABASE CONNECTION
             Statement stmt = conn.createStatement();     // CREATES STATEMENT FOR QUERY
             ResultSet rs = stmt.executeQuery(sql)) {     // EXECUTES QUERY AND STORES RESULT
            while (rs.next()) {
                // CREATES AND ADDS A STUDENT OBJECT FOR EACH ROW IN THE RESULT SET
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("student_number"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("program"),
                        rs.getInt("level")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();  // HANDLES EXCEPTIONS
        }
        return students;          // RETURNS THE LIST OF STUDENTS
    }

    // UPDATES A STUDENT'S PROGRAM AND LEVEL USING THEIR ID
    public void updateStudent(Student student) {
        String sql = "UPDATE students SET program = ?, level = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();              // OPENS DATABASE CONNECTION
             PreparedStatement pstmt = conn.prepareStatement(sql)) {  // PREPARES UPDATE STATEMENT
            pstmt.setString(1, student.getProgram());                 // SETS NEW PROGRAM
            pstmt.setInt(2, student.getLevel());                      // SETS NEW LEVEL
            pstmt.setInt(3, student.getId());                         // SETS STUDENT ID
            pstmt.executeUpdate();                                    // EXECUTES UPDATE
        } catch (SQLException e) {
            e.printStackTrace();                                      // HANDLES EXCEPTIONS
        }
    }

    // DELETES A STUDENT RECORD BY ID
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = Database.getConnection();              // OPENS DATABASE CONNECTION
             PreparedStatement pstmt = conn.prepareStatement(sql)) {  // PREPARES DELETE STATEMENT
            pstmt.setInt(1, id);                                      // SETS THE STUDENT ID TO DELETE
            pstmt.executeUpdate();                                    // EXECUTES DELETE
        } catch (SQLException e) {
            e.printStackTrace();                                      // HANDLES EXCEPTIONS
        }
    }
}
