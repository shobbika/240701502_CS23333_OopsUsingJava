package com.BloodVault.backend.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL  = "jdbc:mysql://localhost:3306/bloodvault?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";          // your MySQL username
    private static final String PASS = "YourPassword!";  // your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // Optional quick test
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
