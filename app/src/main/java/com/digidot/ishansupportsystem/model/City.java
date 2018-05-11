package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class City {
    private String intCityId;
    private String strCityName ;

    public String getIntCityId() {
        return intCityId;
    }

    public void setIntCityId(String intCityId) {
        this.intCityId = intCityId;
    }

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
    }

    @Override
    public String toString() {
        return "City{" +
                "intCityId='" + intCityId + '\'' +
                ", strCityName='" + strCityName + '\'' +
                '}';
    }
}
