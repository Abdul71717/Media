package com.example.media;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;  // <-- This is the missing import

public class DatabaseOperations {
    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id integer PRIMARY KEY,\n"
                + " username text NOT NULL,\n"
                + " password text NOT NULL,\n"
                + " first_name text,\n"
                + " last_name text\n"
                + ");";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
