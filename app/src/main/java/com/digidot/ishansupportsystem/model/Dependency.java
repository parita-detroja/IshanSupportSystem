package com.digidot.ishansupportsystem.model;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class Dependency {
    private String intDependencyId;
    private String strDependency ;

    public String getIntDependencyId() {
        return intDependencyId;
    }

    public void setIntDependencyId(String intDependencyId) {
        this.intDependencyId = intDependencyId;
    }

    public String getStrDependency() {
        return strDependency;
    }

    public void setStrDependency(String strDependency) {
        this.strDependency = strDependency;
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "intDependencyId='" + intDependencyId + '\'' +
                ", strDependency='" + strDependency + '\'' +
                '}';
    }
}
