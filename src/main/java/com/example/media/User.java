package com.example.media;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private List<Post> posts;

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.posts = new ArrayList<>();
    }

    // Getters and setters for each attribute

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

    public void addPost(Post post) {
        posts.add(post);
    }

    public Post getPostById(int id) {
        for (Post post : posts) {
            if (post.getID() == id) {
                return post;
            }
        }
        return null;
    }

    public List<Post> getAllPosts() {
        return new ArrayList<>(posts);
    }

    public List<String> getAllPostContents() {
        return posts.stream().map(Post::getContent).collect(Collectors.toList());
    }

    public boolean deletePostById(int id) {
        return posts.removeIf(post -> post.getID() == id);
    }

    @Override
    public String toString() {
        return "User: " + username + ", Name: " + firstName + " " + lastName + ", Posts Count: " + posts.size();
    }
}
