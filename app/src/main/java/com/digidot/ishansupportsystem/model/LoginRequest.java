package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class LoginRequest {
    private String UserName ;
    private String Password ;
    private String intUserId;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIntUserId() {
        return intUserId;
    }

    public void setIntUserId(String intUserId) {
        this.intUserId = intUserId;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                ", intUserId='" + intUserId + '\'' +
                '}';
    }
}
