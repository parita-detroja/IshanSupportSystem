package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class Zone {
    private String intZoneId;
    private String strZoneName ;

    public String getIntZoneId() {
        return intZoneId;
    }

    public void setIntZoneId(String intZoneId) {
        this.intZoneId = intZoneId;
    }

    public String getStrZoneName() {
        return strZoneName;
    }

    public void setStrZoneName(String strZoneName) {
        this.strZoneName = strZoneName;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "intZoneId='" + intZoneId + '\'' +
                ", strZoneName='" + strZoneName + '\'' +
                '}';
    }
}
