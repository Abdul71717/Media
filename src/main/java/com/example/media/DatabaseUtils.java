package com.example.media;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseUtils {

    /**
     * Creates new database tables for users and posts if they do not already exist.
     */
    public static void createNewTable() {
        createUsersTable(); // Create the users table
        createPostsTable(); // Create the posts table
    }

    /**
     * Creates the 'users' table with appropriate columns if it does not already exist.
     */
    private static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id integer PRIMARY KEY,\n"
                + " username text NOT NULL UNIQUE,\n"
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

    /**
     * Creates the 'posts' table with appropriate columns if it does not already exist.
     */
    private static void createPostsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS posts (\n"
                + " id integer PRIMARY KEY,\n"
                + " content text NOT NULL,\n"
                + " author_id integer,\n"
                + " likes integer DEFAULT 0,\n"
                + " shares integer DEFAULT 0,\n"
                + " date_time text NOT NULL,\n"
                + " FOREIGN KEY (author_id) REFERENCES users(id)\n"
                + ");";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
