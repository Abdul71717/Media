package com.example.media;

import java.util.List;

public class UserManager {

    // Register a new user
    public boolean registerUser(String username, String password, String firstName, String lastName) {
        return DatabaseOperations.addUser(username, password, firstName, lastName);
    }

    // Authenticate a user during login
    public User loginUser(String username, String password) {
        return DatabaseOperations.getUserByUsernameAndPassword(username, password);
    }

    // Edit a user's profile details
    public boolean editUserProfile(int userId, String newFirstName, String newLastName, String newPassword) {
        return DatabaseOperations.updateUser(userId, newPassword, newFirstName, newLastName);
    }

    // Find a user by username
    public User findUserByUsername(String username) {
        return DatabaseOperations.getUserByUsername(username);
    }

    // List all registered users
    public List<User> listAllUsers() {
        return DatabaseOperations.getAllUsers();
    }

    @Override
    public String toString() {
        List<User> allUsers = listAllUsers();
        return "Total Users: " + (allUsers != null ? allUsers.size() : 0);
    }
}
