package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class Client {
    private String intClientId;
    private String strFullName ;

    public String getIntClientId() {
        return intClientId;
    }

    public void setIntClientId(String intClientId) {
        this.intClientId = intClientId;
    }

    public String getStrFullName() {
        return strFullName;
    }

    public void setStrFullName(String strFullName) {
        this.strFullName = strFullName;
    }

    @Override
    public String toString() {
        return "Client{" +
                "intClientId='" + intClientId + '\'' +
                ", strFullName='" + strFullName + '\'' +
                '}';
    }
}
