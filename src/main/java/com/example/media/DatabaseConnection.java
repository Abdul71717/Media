package com.example.media;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String SQLITE_JDBC = "org.sqlite.JDBC";
    private static final String SQLITE_DB_URL = "jdbc:sqlite:mydatabase.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName(SQLITE_JDBC);
            conn = DriverManager.getConnection(SQLITE_DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
