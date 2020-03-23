package com.deathmarch.intersection.model;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postUserId;
    private String postId;
    private String postType;
    private String postImage;
    private String postText;
    private long postTime;

    public Post(String postUserId, String postId, String postType, String postImage, String postText, long postTime) {
        this.postUserId = postUserId;
        this.postId = postId;
        this.postType = postType;
        this.postImage = postImage;
        this.postText = postText;
        this.postTime = postTime;
    }

    public Post() {
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }
}
