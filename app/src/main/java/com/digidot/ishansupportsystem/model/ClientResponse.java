package com.digidot.ishansupportsystem.model;

import java.util.List;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class ClientResponse {

    private List<Client> tblClient;

    public List<Client> getTblClient() {
        return tblClient;
    }

    public void setTblClient(List<Client> tblClient) {
        this.tblClient = tblClient;
    }
}
