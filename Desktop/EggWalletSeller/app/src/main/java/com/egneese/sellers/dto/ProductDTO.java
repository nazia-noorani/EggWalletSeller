package com.egneese.sellers.dto;


import java.util.List;

/**
 * Created by nazianoorani on 20/01/16.
 */


public class ProductDTO {
    private String name;
    private Double price;
    private List<InvestmentDTO> investments;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<InvestmentDTO> getInvestments() {
        return investments;
    }

    public void setInvestments(List<InvestmentDTO> investments) {
        this.investments = investments;
    }
}
