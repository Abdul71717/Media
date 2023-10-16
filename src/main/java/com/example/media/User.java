package com.example.media;

import java.util.List;

public class User {
    private int id; // Unique ID for the user
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public User(int id, String username, String password, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and setters for each attribute

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Methods related to posts

    public List<Post> getAllPosts() {
        return DatabaseOperations.getPostsByUserId(this.id);
    }

    public void addPost(Post post) {
        String content = post.getContent(); // Assuming you have a getContent() method in the Post class
        DatabaseOperations.addPostToDatabase(content, this.id);
    }


    @Override
    public String toString() {
        return "User: " + username + ", Name: " + firstName + " " + lastName;
    }
}
