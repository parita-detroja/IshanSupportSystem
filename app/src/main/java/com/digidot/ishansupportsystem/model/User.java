package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class User {
    private String intNo;
    private String strName ;
    private Integer intAge ;
    private String strGender ;
    private String strPhone ;
    private String stremail ;
    private String Error;

    public String getIntNo() {
        return intNo;
    }

    public void setIntNo(String intNo) {
        this.intNo = intNo;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public Integer getIntAge() {
        return intAge;
    }

    public void setIntAge(Integer intAge) {
        this.intAge = intAge;
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrGender(String strGender) {
        this.strGender = strGender;
    }

    public String getStrPhone() {
        return strPhone;
    }

    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    public String getStremail() {
        return stremail;
    }

    public void setStremail(String stremail) {
        this.stremail = stremail;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}
