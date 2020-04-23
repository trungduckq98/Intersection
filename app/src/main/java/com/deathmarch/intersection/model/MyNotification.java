package com.deathmarch.intersection.model;

public class MyNotification {
    private String postId;
    private String postImage;
    private String postText;
    private String postType;
    private String notifyType;
    private String seen;
    private long time;
    private String userId;

    public MyNotification(String postId, String postImage, String postText, String postType, String notifyType, String seen, long time, String userId) {
        this.postId = postId;
        this.postImage = postImage;
        this.postText = postText;
        this.postType = postType;
        this.notifyType = notifyType;
        this.seen = seen;
        this.time = time;
        this.userId = userId;
    }

    public MyNotification() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MyNotification{" +
                "postId='" + postId + '\'' +
                ", postImage='" + postImage + '\'' +
                ", postText='" + postText + '\'' +
                ", postType='" + postType + '\'' +
                ", notifyType='" + notifyType + '\'' +
                ", seen='" + seen + '\'' +
                ", time=" + time +
                ", userId='" + userId + '\'' +
                '}';
    }
}
