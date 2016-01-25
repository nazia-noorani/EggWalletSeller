package com.egneese.sellers.dto;

/**
 * Created by adityaagrawal on 14/01/16.
 */
public class WalletDTO {
    private Integer id;
    private Double balance;
    private String lastUpdated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "WalletDTO{" +
                "id=" + id +
                ", balance=" + balance +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
