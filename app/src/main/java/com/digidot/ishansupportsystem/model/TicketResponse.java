package com.digidot.ishansupportsystem.model;

import java.util.List;

/**
 * Created by riddhi.parkhiya on 1/25/2018.
 */

public class TicketResponse {

    private List<Ticket> tblTicket;

    public List<Ticket> getTblTicket() {
        return tblTicket;
    }

    public void setTblTicket(List<Ticket> tblTicket) {
        this.tblTicket = tblTicket;
    }
}
