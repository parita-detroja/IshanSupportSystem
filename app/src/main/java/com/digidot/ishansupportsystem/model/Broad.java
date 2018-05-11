package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class Broad {
    private String intBroadCategoryId;
    private String strBroadCategory ;

    public String getIntBroadCategoryId() {
        return intBroadCategoryId;
    }

    public void setIntBroadCategoryId(String intBroadCategoryId) {
        this.intBroadCategoryId = intBroadCategoryId;
    }

    public String getStrBroadCategory() {
        return strBroadCategory;
    }

    public void setStrBroadCategory(String strBroadCategory) {
        this.strBroadCategory = strBroadCategory;
    }

    @Override
    public String toString() {
        return "Broad{" +
                "intBroadCategoryId='" + intBroadCategoryId + '\'' +
                ", strBroadCategory='" + strBroadCategory + '\'' +
                '}';
    }
}
