package com.prabal.studentperformance.db;
import java.sql.*;
public final class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/student_performance_db";
    private static final String USER = "root";
    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";
    private DBConnection() {}
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}