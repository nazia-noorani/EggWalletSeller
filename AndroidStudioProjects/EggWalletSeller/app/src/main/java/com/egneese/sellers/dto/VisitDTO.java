package com.egneese.sellers.dto;

/**
 * Created by nazianoorani on 20/01/16.
 **/
public class VisitDTO {
    private String date;
    private Double paidAmount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }
}
