package com.example.media;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;

    public UserManager() {
        this.users = new ArrayList<>();
    }

    // Register a new user
    public boolean registerUser(String username, String password, String firstName, String lastName) {
        if (findUserByUsername(username) != null) {
            return false; // Username already exists
        }
        User newUser = new User(username, password, firstName, lastName);
        users.add(newUser);
        return true;
    }

    // Authenticate a user during login
    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Successful login
            }
        }
        return null; // Login failed
    }

    // Edit a user's profile details
    public boolean editUserProfile(String username, String newFirstName, String newLastName, String newPassword) {
        User user = findUserByUsername(username);
        if (user != null) {
            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            user.setPassword(newPassword);
            return true; // Profile updated successfully
        }
        return false; // User not found
    }

    // Delete a user
    public boolean deleteUser(String username) {
        return users.removeIf(user -> user.getUsername().equals(username));
    }

    // Find a user by username
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    // List all registered users
    public List<User> listAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public String toString() {
        return "Total Users: " + users.size();
    }
}
