package com.example.media;

import java.util.List;

public class UserManager {

    private UserDAO userDAO;

    public UserManager() {
        this.userDAO = new UserDAOImpl();
    }

    // Register a new user
    public boolean registerUser(String username, String password, String firstName, String lastName) {
        User user = new User(0, username, password, firstName, lastName); // Assuming ID is not needed for registration
        return userDAO.addUser(user);
    }

    // Authenticate a user during login
    public User loginUser(String username, String password) {
        return userDAO.getUserByUsernameAndPassword(username, password);
    }

    // Edit a user's profile details
    public boolean updateUserProfile(int userId, String newUsername, String newPassword, String newFirstName, String newLastName) {
        User user = new User(userId, newUsername, newPassword, newFirstName, newLastName);
        return userDAO.updateUser(user);
    }

    // Find a user by username
    public User findUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    // List all registered users
    public List<User> listAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public String toString() {
        List<User> allUsers = listAllUsers();
        return "Total Users: " + (allUsers != null ? allUsers.size() : 0);
    }
}
