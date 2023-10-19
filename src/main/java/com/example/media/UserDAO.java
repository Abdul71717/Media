package com.example.media;


import java.util.List;

public interface UserDAO {
    boolean addUser(User user);
    User getUserByUsernameAndPassword(String username, String password);
    User getUserByUsername(String username);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    List<User> getAllUsers();
    boolean usernameExists(String username);  // Ensure this line is present

    int getUserIdByUsername(String username);




}