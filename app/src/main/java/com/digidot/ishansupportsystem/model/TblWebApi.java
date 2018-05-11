package com.digidot.ishansupportsystem.model;

/**
 * Created by ABC on 25-01-2018.
 */

public class TblWebApi {
    private String APILink;
    private String WebsiteLink;

    public String getAPILink() {
        return APILink;
    }

    public void setAPILink(String APILink) {
        this.APILink = APILink;
    }

    public String getWebsiteLink() {
        return WebsiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        WebsiteLink = websiteLink;
    }

    @Override
    public String toString() {
        return "TblWebApi{" +
                "APILink='" + APILink + '\'' +
                ", WebsiteLink='" + WebsiteLink + '\'' +
                '}';
    }
}
