package com.student.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StudentCrud {

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS Student (name VARCHAR(255) PRIMARY KEY NOT NULL, class VARCHAR(255) NOT NULL, mark INT NOT NULL)";
    private static final String INSERT_STUDENT_QUERY = "INSERT INTO Student (name, class, mark) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_STUDENTS_QUERY = "SELECT * FROM Student";
    private static final String UPDATE_STUDENT_QUERY = "UPDATE Student SET class=?, mark=? WHERE name=?";
    private static final String DELETE_STUDENT_QUERY = "DELETE FROM Student WHERE name=?";

    private static List<Map<String, Object>> studentList = new ArrayList<>();

    public static void main(String[] args) throws SQLException {
        createTable();

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Create Student");
            System.out.println("2. Read Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createStudent();
                    break;
                case 2:
                    getStudents();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    System.out.println("Exiting program. Goodbye!");
                    DatabaseUtils.closeConnection();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void createTable() {
        try (Connection connection = DatabaseUtils.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_QUERY);
            DatabaseUtils.closeConnection();
        } catch (SQLException e) {
        	DatabaseUtils.closeConnection();
            System.out.println("Table creation failed");
            e.printStackTrace();
        }
    }

    private static void createStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student class: ");
        String className = scanner.nextLine();
        System.out.print("Enter student mark: ");
        int mark = scanner.nextInt();

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT_QUERY)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, className);
            preparedStatement.setInt(3, mark);
            preparedStatement.executeUpdate();

            System.out.println("Student created successfully.");
            DatabaseUtils.closeConnection();
        } catch (SQLException e) {
        	DatabaseUtils.closeConnection();
            System.out.println("Error in creating student");
            e.printStackTrace();
        }
    }

    private static List<Map<String, Object>> readStudents() {
        List<Map<String, Object>> studentsList = new ArrayList<>();
        try (Connection connection = DatabaseUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_STUDENTS_QUERY)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> student = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValueObject = resultSet.getObject(i);

                    if (columnValueObject instanceof Number) {
                        Number columnValue = (Number) columnValueObject;
                        student.put(columnName, Math.abs(columnValue.intValue()));
                    } else {
                        student.put(columnName, columnValueObject);
                    }
                }

                studentsList.add(student);
            }
            DatabaseUtils.closeConnection();
        } catch (SQLException e) {
        	DatabaseUtils.closeConnection();
            System.out.println("Error in readStudents");
            e.printStackTrace();
        }

        return studentsList;
        
    }

    private static void getStudents() {
        List<Map<String, Object>> studentsList = readStudents();
        System.out.println("List of Students:");
        System.out.println(studentsList);
    }

    private static void updateStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student name to update: ");
        String name = scanner.next();
        System.out.print("Enter new class: ");
        String newClass = scanner.next();
        System.out.print("Enter new mark: ");
        int newMark = scanner.nextInt();

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STUDENT_QUERY)) {
            preparedStatement.setString(1, newClass);
            preparedStatement.setInt(2, newMark);
            preparedStatement.setString(3, name);
            preparedStatement.executeUpdate();
            DatabaseUtils.closeConnection();
        } catch (SQLException e) {
        	DatabaseUtils.closeConnection();
            System.out.println("Error in updating student");
            e.printStackTrace();
            
            
        }

        System.out.println("Student updated successfully.");
    }
    
    private static void deleteStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student name to delete: ");
        String name = scanner.next();

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT_QUERY)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            DatabaseUtils.closeConnection();
        } catch (SQLException e) {
        	DatabaseUtils.closeConnection();
            System.out.println("Error in deleting student");
            e.printStackTrace();
        }

        System.out.println("Student deleted successfully.");
    }
}
