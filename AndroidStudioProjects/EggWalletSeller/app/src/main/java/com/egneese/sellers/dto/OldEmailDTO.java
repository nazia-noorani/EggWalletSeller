package com.egneese.sellers.dto;

/**
 * Created by nazianoorani on 21/01/16.
 */

import java.util.Date;
public class OldEmailDTO {
    private String email;
    private Date added;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
