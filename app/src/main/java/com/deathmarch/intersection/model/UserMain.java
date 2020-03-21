package com.deathmarch.intersection.model;

import java.util.Objects;

public class UserMain {
    private String userId;
    private String userEmail;
    private String userPhoneNumber;
    private String userDisplayName;
    private String userImage;

    public UserMain(String userId, String userEmail, String userPhoneNumber, String userDisplayName, String userImage) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userDisplayName = userDisplayName;
        this.userImage = userImage;
    }

    public UserMain() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserMain)) return false;
        UserMain userMain = (UserMain) o;
        return getUserId().equals(userMain.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }
}
