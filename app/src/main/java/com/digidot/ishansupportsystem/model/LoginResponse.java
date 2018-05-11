package com.digidot.ishansupportsystem.model;

import java.util.List;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class LoginResponse {

    private List<LoginRequest> tblLogin;

    public List<LoginRequest> getTblLogin() {
        return tblLogin;
    }

    public void setTblLogin(List<LoginRequest> tblLogin) {
        this.tblLogin = tblLogin;
    }
}
