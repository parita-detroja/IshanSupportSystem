package com.digidot.ishansupportsystem.model;

import java.util.List;

public class NotificationResponse {

    private List<Notification> tblNotifications;

    public List<Notification> getTblNotifications() {
        return tblNotifications;
    }

    public void setTblNotifications(List<Notification> tblNotifications) {
        this.tblNotifications = tblNotifications;
    }
}
