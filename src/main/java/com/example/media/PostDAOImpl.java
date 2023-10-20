package com.example.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the PostDAO interface for managing posts in the data access layer.
 */
public class PostDAOImpl implements PostDAO {

    private UserDAO userDAO = new UserDAOImpl(); // Assumes you have a UserDAOImpl class

    @Override
    public boolean addPost(Post post) {
        // Get the author's ID by username using UserDAO
        int authorId = userDAO.getUserIdByUsername(post.getAuthor());
        return addPost(post.getID(), post.getContent(), authorId, post.getLikes(), post.getShares(), post.getDateTime());
    }

    @Override
    public boolean addPost(int postId, String content, int authorId, int likes, int shares, String dateTime) {
        String sql = "INSERT INTO posts(id, content, author_id, likes, shares, date_time) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setString(2, content);
            pstmt.setInt(3, authorId);
            pstmt.setInt(4, likes);
            pstmt.setInt(5, shares);
            pstmt.setString(6, dateTime);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletePost(int postId, int userId) {
        String sql = "DELETE FROM posts WHERE id = ? AND author_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Post> getPostsByUserId(int userId) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.content, p.likes, p.shares, p.date_time, u.username FROM posts p JOIN users u ON p.author_id = u.id WHERE p.author_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Post post = new Post(rs.getInt("id"), rs.getString("content"), rs.getString("username"), rs.getInt("likes"), rs.getInt("shares"), rs.getString("date_time"));
                posts.add(post);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.content, p.likes, p.shares, p.date_time, u.username FROM posts p JOIN users u ON p.author_id = u.id";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Post post = new Post(rs.getInt("id"), rs.getString("content"), rs.getString("username"), rs.getInt("likes"), rs.getInt("shares"), rs.getString("date_time"));
                posts.add(post);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    @Override
    public List<Post> getAllPostsSortedByLikes() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.content, p.likes, p.shares, p.date_time, u.username FROM posts p JOIN users u ON p.author_id = u.id ORDER BY p.likes DESC";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Post post = new Post(rs.getInt("id"), rs.getString("content"), rs.getString("username"), rs.getInt("likes"), rs.getInt("shares"), rs.getString("date_time"));
                posts.add(post);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    @Override
    public List<Post> getAllPostsSortedByShares() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.content, p.likes, p.shares, u.username FROM posts p JOIN users u ON p.author_id = u.id ORDER BY p.shares DESC";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Post post = new Post(rs.getInt("id"), rs.getString("content"), rs.getString("username"), rs.getInt("likes"), rs.getInt("shares"), null); // No dateTime
                posts.add(post);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    @Override
    public Post getPostById(int postId) {
        String sql = "SELECT p.id, p.content, p.likes, p.shares, p.date_time, u.username FROM posts p JOIN users u ON p.author_id = u.id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Post(rs.getInt("id"), rs.getString("content"), rs.getString("username"), rs.getInt("likes"), rs.getInt("shares"), rs.getString("date_time"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
