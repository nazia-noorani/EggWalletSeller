package com.egneese.sellers.dto;

/**
 * Created by nazianoorani on 20/01/16.
 */
public class InvestmentDTO {
    private String date;
    private Double amount;
    private Integer stock;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
