package com.digidot.ishansupportsystem.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABC on 25-01-2018.
 */

public class EndPoint {

    private List<TblWebApi> tblWebApi;

    public List<TblWebApi> getTblWebApi() {
        return tblWebApi;
    }

    public void setTblWebApi(ArrayList<TblWebApi> tblWebApi) {
        this.tblWebApi = tblWebApi;
    }
}
