package com.example.media;

import java.util.List;

/**
 * This interface defines the operations for managing users in the data access layer.
 */
public interface UserDAO {

    /**
     * Adds a new user to the database.
     *
     * @param user The User object to be added.
     * @return true if the user was added successfully, false otherwise.
     */
    boolean addUser(User user);

    /**
     * Retrieves a user by their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The User object representing the user with the provided username and password, or null if not found.
     */
    User getUserByUsernameAndPassword(String username, String password);

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return The User object representing the user with the provided username, or null if not found.
     */
    User getUserByUsername(String username);

    /**
     * Updates an existing user's information.
     *
     * @param user The updated User object.
     * @return true if the user was updated successfully, false otherwise.
     */
    boolean updateUser(User user);

    /**
     * Deletes a user from the database.
     *
     * @param userId The ID of the user to delete.
     * @return true if the user was deleted successfully, false otherwise.
     */
    boolean deleteUser(int userId);

    /**
     * Retrieves a list of all users.
     *
     * @return A list of User objects representing all users.
     */
    List<User> getAllUsers();

    /**
     * Checks if a username already exists in the database.
     *
     * @param username The username to check for existence.
     * @return true if the username exists, false otherwise.
     */
    boolean usernameExists(String username);

    /**
     * Retrieves the user's ID by their username.
     *
     * @param username The username of the user.
     * @return The ID of the user with the provided username, or -1 if not found.
     */
    int getUserIdByUsername(String username);
}
