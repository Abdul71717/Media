package com.example.media;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;

    public UserManager() {
        this.users = new ArrayList<>();
        loadDataFromCSV(); // Load data from CSV when UserManager is instantiated
    }

    // Register a new user
    public boolean registerUser(String username, String password, String firstName, String lastName) {
        if (findUserByUsername(username) != null) {
            return false; // Username already exists
        }
        User newUser = new User(username, password, firstName, lastName);
        users.add(newUser);
        saveDataToCSV(); // Save data to CSV after registering a new user
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
            saveDataToCSV(); // Save data to CSV after editing a user profile
            return true; // Profile updated successfully
        }
        return false; // User not found
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

    public void saveDataToCSV() {
        try (PrintWriter writer = new PrintWriter(new File("data.csv"))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Username,Password,FirstName,LastName\n"); // header
            for (User user : users) {
                sb.append(user.getUsername());
                sb.append(',');
                sb.append(user.getPassword());
                sb.append(',');
                sb.append(user.getFirstName());
                sb.append(',');
                sb.append(user.getLastName());
                sb.append('\n');
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            boolean header = true; // to skip the header line
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                String[] values = line.split(",");
                User user = new User(values[0], values[1], values[2], values[3]);
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
