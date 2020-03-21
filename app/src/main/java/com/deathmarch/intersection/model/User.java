package com.deathmarch.intersection.model;

import java.util.Objects;

public class User {
    private UserMain userMain;
    private UserInfo userInfo;
    private UserState userState;

    public User(UserMain userMain, UserInfo userInfo, UserState userState) {
        this.userMain = userMain;
        this.userInfo = userInfo;
        this.userState = userState;
    }

    public User() {
    }

    public User(UserMain userMain, UserInfo userInfo) {
        this.userMain = userMain;
        this.userInfo = userInfo;
    }

    public UserMain getUserMain() {
        return userMain;
    }

    public void setUserMain(UserMain userMain) {
        this.userMain = userMain;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserMain().equals(user.getUserMain());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserMain());
    }
}
