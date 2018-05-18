package com.digidot.ishansupportsystem.model;

public class Notification {
    private String intNotificationId;
    private String strNotificationTitle;
    private String strDetails;
    private String dtCreatedOn;

    public String getIntNotificationId() {
        return intNotificationId;
    }

    public void setIntNotificationId(String intNotificationId) {
        this.intNotificationId = intNotificationId;
    }

    public String getStrNotificationTitle() {
        return strNotificationTitle;
    }

    public void setStrNotificationTitle(String strNotificationTitle) {
        this.strNotificationTitle = strNotificationTitle;
    }

    public String getStrDetails() {
        return strDetails;
    }

    public void setStrDetails(String strDetails) {
        this.strDetails = strDetails;
    }

    public String getDtCreatedOn() {
        return dtCreatedOn;
    }

    public void setDtCreatedOn(String dtCreatedOn) {
        this.dtCreatedOn = dtCreatedOn;
    }
}
