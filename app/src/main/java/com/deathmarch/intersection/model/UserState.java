package com.deathmarch.intersection.model;

public class UserState {
    private String userState;
    private long userTimeState;

    public UserState(String userState, long userTimeState) {
        this.userState = userState;
        this.userTimeState = userTimeState;
    }

    public UserState() {
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public long getUserTimeState() {
        return userTimeState;
    }

    public void setUserTimeState(long userTimeState) {
        this.userTimeState = userTimeState;
    }
}
