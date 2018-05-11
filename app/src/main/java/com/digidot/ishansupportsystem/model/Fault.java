package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class Fault {
    private String intFaultId;
    private String strFault ;

    public String getIntFaultId() {
        return intFaultId;
    }

    public void setIntFaultId(String intFaultId) {
        this.intFaultId = intFaultId;
    }

    public String getStrFault() {
        return strFault;
    }

    public void setStrFault(String strFault) {
        this.strFault = strFault;
    }

    @Override
    public String toString() {
        return "Fault{" +
                "intFaultId='" + intFaultId + '\'' +
                ", strFault='" + strFault + '\'' +
                '}';
    }
}
