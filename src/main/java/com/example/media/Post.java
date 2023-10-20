package com.example.media;

public class Post {
    private int ID;
    private String content;
    private String author;
    private int likes;
    private int shares;
    private String dateTime; // Changed from LocalDateTime to String

    // Constructor for creating a Post object from database records
    public Post(int ID, String content, String author, int likes, int shares, String dateTime) {
        this.ID = ID;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.dateTime = dateTime; // Accepts dateTime as a string
    }

    // Constructor for creating a new Post
    public Post(String content, String author) {
        this.content = content;
        this.author = author;
        this.likes = 0; // Initialize likes to 0 for a new post
        this.shares = 0; // Initialize shares to 0 for a new post
        this.dateTime = null; // Initialize dateTime to null for a new post
    }

    // Getters and setters for each attribute

    public int getID() {
        return ID;
    }



    public String getContent() {
        return content;
    }


    public String getAuthor() {
        return author;
    }


    public int getLikes() {
        return likes;
    }



    public int getShares() {
        return shares;
    }



    public String getDateTime() {
        return dateTime;
    }



    @Override
    public String toString() {
        return "Post ID: " + ID + ", Author: " + author + ", Content: " + content + ", Likes: " + likes + ", Shares: " + shares + ", Date: " + dateTime;
    }
}
