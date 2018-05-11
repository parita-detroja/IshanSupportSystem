package com.digidot.ishansupportsystem.model;

import java.util.List;

/**
 * Created by ABC on 26-01-2018.
 */

public class UserResponse {
    private List<User> tblRegistration;

    public List<User> getTblRegistration() {
        return tblRegistration;
    }

    public void setTblRegistration(List<User> tblRegistration) {
        this.tblRegistration = tblRegistration;
    }
}
