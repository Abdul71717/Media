package com.example.media;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String SQLITE_JDBC = "org.sqlite.JDBC";
    private static final String SQLITE_DB_URL = "jdbc:sqlite:mydatabase.db";

    /**
     * Establishes a connection to the SQLite database.
     *
     * @return A Connection object if the connection is successful, null otherwise.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName(SQLITE_JDBC);
            conn = DriverManager.getConnection(SQLITE_DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error during database connection: " + e.getMessage());
            // Consider logging the error or throwing a custom exception here.
        }
        return conn;
    }
}
