package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class Resolution {
    private String intResolutionId;
    private String strResolutionCode ;
    private String intBroadCategoryId;

    public String getIntResolutionId() {
        return intResolutionId;
    }

    public void setIntResolutionId(String intResolutionId) {
        this.intResolutionId = intResolutionId;
    }

    public String getStrResolutionCode() {
        return strResolutionCode;
    }

    public void setStrResolutionCode(String strResolutionCode) {
        this.strResolutionCode = strResolutionCode;
    }

    public String getIntBroadCategoryId() {
        return intBroadCategoryId;
    }

    public void setIntBroadCategoryId(String intBroadCategoryId) {
        this.intBroadCategoryId = intBroadCategoryId;
    }

    @Override
    public String toString() {
        return "Resolution{" +
                "intResolutionId='" + intResolutionId + '\'' +
                ", strResolutionCode='" + strResolutionCode + '\'' +
                ", intBroadCategoryId='" + intBroadCategoryId + '\'' +
                '}';
    }
}
