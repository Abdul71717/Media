package com.example.media;

import java.time.LocalDateTime;

public class Post {
    private int ID;
    private String content;
    private String author;
    private int likes;
    private int shares;
    private LocalDateTime dateTime;

    // Constructor for creating a Post object from database records
    public Post(int ID, String content, String author, int likes, int shares, LocalDateTime dateTime) {
        this.ID = ID;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.dateTime = dateTime;
    }

    // Constructor for creating a new Post
    public Post(String content, String author) {
        this.content = content;
        this.author = author;
        this.likes = 0; // Initialize likes to 0 for a new post
        this.shares = 0; // Initialize shares to 0 for a new post
        this.dateTime = LocalDateTime.now(); // Capture the current date and time
    }

    // Getters and setters for each attribute

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public int getShares() {
        return shares;
    }

    public void incrementShares() {
        this.shares++;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Post ID: " + ID + ", Author: " + author + ", Content: " + content + ", Likes: " + likes + ", Shares: " + shares + ", Date: " + dateTime;
    }
}
