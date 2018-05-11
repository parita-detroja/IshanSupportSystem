package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class State {
    private String intStateId;
    private String strStateName ;

    public String getIntStateId() {
        return intStateId;
    }

    public void setIntStateId(String intStateId) {
        this.intStateId = intStateId;
    }

    public String getStrStateName() {
        return strStateName;
    }

    public void setStrStateName(String strStateName) {
        this.strStateName = strStateName;
    }

    @Override
    public String toString() {
        return "State{" +
                "intStateId='" + intStateId + '\'' +
                ", strStateName='" + strStateName + '\'' +
                '}';
    }
}
