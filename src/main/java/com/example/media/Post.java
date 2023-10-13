package com.example.media;

import java.time.LocalDateTime;

public class Post {
    private static int counter = 0; // To auto-generate unique post IDs
    private int ID;
    private String content;
    private String author;
    private int likes;
    private int shares;
    private LocalDateTime dateTime;

    public Post(String content, String author) {
        this.ID = ++counter; // Auto-increment the ID for each new post
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

    @Override
    public String toString() {
        return "Post ID: " + ID + ", Author: " + author + ", Content: " + content + ", Likes: " + likes + ", Shares: " + shares + ", Date: " + dateTime;
    }
}
