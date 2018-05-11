package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class Ticket {
    private String OfficeId;
    private String FaultId;
    private String Description;
    private String UserId ;
    private String TicketId;
    private String Date;

    public String getOfficeId() {
        return OfficeId;
    }

    public void setOfficeId(String officeId) {
        OfficeId = officeId;
    }

    public String getFaultId() {
        return FaultId;
    }

    public void setFaultId(String faultId) {
        FaultId = faultId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "OfficeId='" + OfficeId + '\'' +
                ", FaultId='" + FaultId + '\'' +
                ", Description='" + Description + '\'' +
                ", UserId='" + UserId + '\'' +
                '}';
    }
}
