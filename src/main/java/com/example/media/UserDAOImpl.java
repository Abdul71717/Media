package com.example.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDAO interface for managing users in the data access layer.
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public boolean addUser(User user) {
        return addUserInternal(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
    }

    /**
     * Adds a new user to the database with individual attributes.
     *
     * @param username   The username of the user.
     * @param password   The password of the user.
     * @param firstName  The first name of the user.
     * @param lastName   The last name of the user.
     * @return true if the user was added successfully, false otherwise.
     */
    public boolean addUserInternal(String username, String password, String firstName, String lastName) {
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

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
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

    @Override
    public User getUserByUsername(String username) {
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

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, first_name, last_name FROM users";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        return updateUserInternal(user.getId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
    }

    /**
     * Updates an existing user's information.
     *
     * @param userId      The ID of the user to update.
     * @param newUsername The new username for the user.
     * @param newPassword The new password for the user.
     * @param newFirstName The new first name for the user.
     * @param newLastName The new last name for the user.
     * @return true if the user was updated successfully, false otherwise.
     */
    public boolean updateUserInternal(int userId, String newUsername, String newPassword, String newFirstName, String newLastName) {
        String sql = "UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setString(3, newFirstName);
            pstmt.setString(4, newLastName);
            pstmt.setInt(5, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
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

    @Override
    public boolean usernameExists(String username) {
        String sql = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int getUserIdByUsername(String username) {
        // Implement the logic for retrieving the user's ID by their username here
        return 0;
    }
}
