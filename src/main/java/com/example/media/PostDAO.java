package com.example.media;

import java.util.List;

public interface PostDAO {
    boolean addPost(Post post);

    boolean addPost(int postId, String content, int authorId, int likes, int shares, String dateTime);

    boolean updatePost(int postId, String content);

    boolean deletePost(int postId, int userId);

    List<Post> getPostsByUserId(int userId);


}