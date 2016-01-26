package com.egneese.sellers.dto;

/**
 * Created by nazianoorani on 20/01/16.
 */

import java.util.List;
public class CustomerDTO {
    private String userId;
    private List<VisitDTO> visits;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<VisitDTO> getVisits() {
        return visits;
    }

    public void setVisits(List<VisitDTO> visits) {
        this.visits = visits;
    }
}
