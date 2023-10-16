package com.example.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    public static void createNewTable() {
        createUsersTable();
        createPostsTable();
    }

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

    public static boolean updateUser(int userId, String password, String firstName, String lastName) {
        String sql = "UPDATE users SET password = ?, first_name = ?, last_name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, password);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setInt(4, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean updatePost(int postId, String content) {
        String sql = "UPDATE posts SET content = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.setInt(2, postId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean deletePost(int postId) {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static List<Post> getPostsByUserId(int userId) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.content, p.likes, p.shares, p.date_time, u.username FROM posts p JOIN users u ON p.author_id = u.id WHERE p.author_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Post post = new Post(rs.getInt("id"), rs.getString("content"), rs.getString("username"), rs.getInt("likes"), rs.getInt("shares"), LocalDateTime.parse(rs.getString("date_time")));
                posts.add(post);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    public static boolean addUser(String username, String password, String firstName, String lastName) {
        String sql = "INSERT INTO users(username, password, first_name, last_name) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static User getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT id, username, password, first_name, last_name FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, first_name, last_name FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, first_name, last_name FROM users";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }


    public static boolean addPostToDatabase(String content, int authorId) {
        String sql = "INSERT INTO posts(content, author_id, date_time) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.setInt(2, authorId);
            pstmt.setString(3, LocalDateTime.now().toString()); // Storing the current date and time
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
