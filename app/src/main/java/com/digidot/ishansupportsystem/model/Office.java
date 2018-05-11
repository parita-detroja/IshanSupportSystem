package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class Office {
    private String intOfficeId;
    private String strOfficeName ;

    public String getIntOfficeId() {
        return intOfficeId;
    }

    public void setIntOfficeId(String intOfficeId) {
        this.intOfficeId = intOfficeId;
    }

    public String getStrOfficeName() {
        return strOfficeName;
    }

    public void setStrOfficeName(String strOfficeName) {
        this.strOfficeName = strOfficeName;
    }

    @Override
    public String toString() {
        return "Office{" +
                "intOfficeId='" + intOfficeId + '\'' +
                ", strOfficeName='" + strOfficeName + '\'' +
                '}';
    }
}
