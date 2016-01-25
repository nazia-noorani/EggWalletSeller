package com.egneese.sellers.dto;

/**
 * Created by nazianoorani on 21/01/16.
 */

import java.util.Date;
public class OldMobileDTO {
    private String mobile;
    private Date added;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
