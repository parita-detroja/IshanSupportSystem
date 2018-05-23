package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class LoginRequest {
    private String UserName ;
    private String Password ;
    private String intUserId;
    private String isCreate;
    private String isView;
    private String isViewOthers;
    private String isUpdateStatus;
    private String isActive;

    public String getIsCreate() {
        return isCreate;
    }

    public void setIsCreate(String isCreate) {
        this.isCreate = isCreate;
    }

    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getIsViewOthers() {
        return isViewOthers;
    }

    public void setIsViewOthers(String isViewOthers) {
        this.isViewOthers = isViewOthers;
    }

    public String getIsUpdateStatus() {
        return isUpdateStatus;
    }

    public void setIsUpdateStatus(String isUpdateStatus) {
        this.isUpdateStatus = isUpdateStatus;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

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
