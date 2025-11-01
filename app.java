/*
 * Simple database application.
 * Used Lecture 12 slides as baseplate.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Scanner;

public class app {
    // Connection to Postgres; used in functions
    public static Connection conn;

    public static void main(String[] args) {
        // JDBC & Database credentials
        String url = "jdbc:postgresql://localhost:5432/A3_1_Students";
        String user = "postgres";
        String password = "password";

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");
            } else {
                System.out.println("Failed to establish connection.");
            }

            doDemoSequence();

            // Close connection to Postgres
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAllStudents() {
        // Retrieves and displays all records from the students table.

        try {
            Statement stmt = conn.createStatement();
            String SQL = "SELECT * FROM students";
            ResultSet rs = stmt.executeQuery(SQL);
            while(rs.next()){
                // Read columns from row
                int     studentId       = rs.getInt("student_id");
                String  firstName       = rs.getString("first_name");
                String  lastName        = rs.getString("last_name");
                String  email           = rs.getString("email");
                Date    enrollmentDate  = rs.getDate("enrollment_date");
                // Display
                System.out.printf("%-2d %-8s %-8s %-25s %-10s\n", 
                    studentId, firstName, lastName, email, enrollmentDate.toString());
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    public static boolean addStudent(String firstName, String lastName, String email, String enrollmentDate) {
        // Inserts a new student record into the students table.

        try {
            Statement stmt = conn.createStatement();
            String insertStatement = String.format("INSERT INTO students VALUES (DEFAULT, '%s', '%s', '%s', '%s');",
                firstName, lastName, email, enrollmentDate);
            int rowsInserted = stmt.executeUpdate(insertStatement);
            if (rowsInserted > 0) {
                System.out.printf("Successfully inserted student with values %s %s %s %s!\n",
                    firstName, lastName, email, enrollmentDate);
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 

        System.out.printf("Failed to insert student with values %s %s %s %s.\n",
            firstName, lastName, email, enrollmentDate);
        return false;
    }

    public static boolean updateStudentEmail(int studentId, String newEmail) {
        // Updates the email address for a student with the specified student_id.

        try {
            Statement stmt = conn.createStatement();
            String insertStatement = String.format("UPDATE students SET email = '%s' WHERE student_id = %d",
                newEmail, studentId);
            int rowsUpdated = stmt.executeUpdate(insertStatement);
            if (rowsUpdated > 0) {
                System.out.printf("Successfully updated email of student with id %d to %s!\n",
                    studentId, newEmail);
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 

        System.out.printf("Failed to update email of student with id %d to %s.\n",
            studentId, newEmail);
        return false;
    }

    public static boolean deleteStudent(int student_id) {
        // Deletes the record of the student with the specified student_id.

        try {
            Statement stmt = conn.createStatement();
            String insertStatement = String.format("DELETE FROM students WHERE student_id = %d",
                student_id);
            int rowsDeleted = stmt.executeUpdate(insertStatement);
            if (rowsDeleted > 0) {
                System.out.printf("Successfully deleted student with id %d!\n", 
                    student_id);
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 

        System.out.printf("Failed to delete student with id %d.\n", 
            student_id);
        return false;
    }

    public static void doDemoSequence() {
        // Performs CRUD operations in sequence, waiting for user input between operations.

        // Scanner to allow wait between CRUD operations
        Scanner scanner = new Scanner(System.in);

        // Run CRUD functions
        System.out.println("\nPress enter to perform getAllStudents()");
        scanner.nextLine();
        getAllStudents();

        System.out.println("\nPress enter to perform addStudent() [INSERT]");
        scanner.nextLine();
        addStudent("First", "Last", "email", "2025-10-29");
        getAllStudents();
        
        System.out.println("\nPress enter to perform updateStudentEmail() [UPDATE]");
        scanner.nextLine();
        updateStudentEmail(4, "updatedEmail");
        getAllStudents();

        System.out.println("\nPress enter to perform deleteStudent() [DELETE]");
        scanner.nextLine();
        deleteStudent(4);
        getAllStudents();

        System.out.println();
        scanner.close();
    }

}