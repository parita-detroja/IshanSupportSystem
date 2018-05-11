package com.digidot.ishansupportsystem.model;

import java.util.List;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class BroadResponse {

    private List<Broad> tblBroadCategory;

    public List<Broad> getTblBroadCategory() {
        return tblBroadCategory;
    }

    public void setTblBroadCategory(List<Broad> tblBroadCategory) {
        this.tblBroadCategory = tblBroadCategory;
    }

    @Override
    public String toString() {
        return "BroadResponse{" +
                "tblBroadCategory=" + tblBroadCategory +
                '}';
    }
}
