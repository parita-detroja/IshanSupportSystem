package com.digidot.ishansupportsystem.model;

import java.util.List;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class FaultResponse {

    private List<Fault> tblFault;

    public List<Fault> getTblFault() {
        return tblFault;
    }

    public void setTblFault(List<Fault> tblFault) {
        this.tblFault = tblFault;
    }
}
