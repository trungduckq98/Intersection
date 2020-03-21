package com.deathmarch.intersection.model;

public class UserInfo {
    private String userFullName;
    private String userDescription;
    private String userDateOfbirth;
    private String userAddress;
    private String userSex;

    public UserInfo(String userFullName, String userDescription, String userDateOfbirth, String userAddress, String userSex) {
        this.userFullName = userFullName;
        this.userDescription = userDescription;
        this.userDateOfbirth = userDateOfbirth;
        this.userAddress = userAddress;
        this.userSex = userSex;
    }

    public UserInfo() {
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserDateOfbirth() {
        return userDateOfbirth;
    }

    public void setUserDateOfbirth(String userDateOfbirth) {
        this.userDateOfbirth = userDateOfbirth;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }


}
