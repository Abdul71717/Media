package com.example.media;

import java.util.List;

/**
 * This interface defines the operations for managing posts in the data access layer.
 */
public interface PostDAO {

    /**
     * Adds a new post to the database using a Post object.
     *
     * @param post The Post object to be added.
     * @return true if the post was added successfully, false otherwise.
     */
    boolean addPost(Post post);

    /**
     * Adds a new post to the database with individual attributes.
     *
     * @param postId    The unique identifier for the post.
     * @param content   The content of the post.
     * @param authorId  The ID of the author of the post.
     * @param likes     The number of likes for the post.
     * @param shares    The number of shares for the post.
     * @param dateTime  The date and time of the post.
     * @return true if the post was added successfully, false otherwise.
     */
    boolean addPost(int postId, String content, int authorId, int likes, int shares, String dateTime);

    /**
     * Retrieves a list of all posts.
     *
     * @return A list of Post objects representing all posts.
     */
    List<Post> getAllPosts();

    /**
     * Retrieves a list of all posts sorted by the number of likes.
     *
     * @return A list of Post objects representing posts sorted by likes.
     */
    List<Post> getAllPostsSortedByLikes();

    /**
     * Retrieves a list of all posts sorted by the number of shares.
     *
     * @return A list of Post objects representing posts sorted by shares.
     */
    List<Post> getAllPostsSortedByShares();

    /**
     * Retrieves a post by its unique identifier.
     *
     * @param postId The ID of the post to retrieve.
     * @return The Post object representing the specified post, or null if not found.
     */
    Post getPostById(int postId);

    /**
     * Deletes a post from the database.
     *
     * @param postId The ID of the post to delete.
     * @param userId The ID of the user attempting to delete the post.
     * @return true if the post was deleted successfully, false otherwise.
     */
    boolean deletePost(int postId, int userId);

    /**
     * Retrieves a list of posts authored by a specific user.
     *
     * @param userId The ID of the user to retrieve posts for.
     * @return A list of Post objects representing posts authored by the specified user.
     */
    List<Post> getPostsByUserId(int userId);
}
