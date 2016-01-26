package com.egneese.sellers.dto;

import java.util.List;

/**
 * Created by nazianoorani on 20/01/16.
 */
public class SellerDTO {
    private List<ProductDTO> products;
    private List<CustomerDTO> customers;
    private List<SecondaryMobileDTO> secondaryMobiles;
    private List<DueDTO> dues;
    private List<SellerTransactionDTO> sellerTransactions;
    private String realm;
    private String name;
    private String mobile;
    private String shopType;
    private String accessToken;
    private String email;
    private String id;
    private String lastUpdated;
    private String shopName;
    private WalletDTO wallet;

    public WalletDTO getWallet() {
        return wallet;
    }

    public void setWallet(WalletDTO wallet) {
        this.wallet = wallet;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public List<CustomerDTO> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerDTO> customers) {
        this.customers = customers;
    }

    public List<SecondaryMobileDTO> getSecondaryMobiles() {
        return secondaryMobiles;
    }

    public void setSecondaryMobiles(List<SecondaryMobileDTO> secondaryMobiles) {
        this.secondaryMobiles = secondaryMobiles;
    }

    public List<DueDTO> getDues() {
        return dues;
    }

    public void setDues(List<DueDTO> dues) {
        this.dues = dues;
    }

    public List<SellerTransactionDTO> getSellerTransactions() {
        return sellerTransactions;
    }

    public void setSellerTransactions(List<SellerTransactionDTO> sellerTransactions) {
        this.sellerTransactions = sellerTransactions;
    }

    @Override
    public String toString() {
        return "SellerDTO{" +
                "products=" + products +
                ", customers=" + customers +
                ", secondaryMobiles=" + secondaryMobiles +
                ", dues=" + dues +
                ", sellerTransactions=" + sellerTransactions +
                ", realm='" + realm + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", shopType='" + shopType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", shopName='" + shopName + '\'' +
                '}';
    }
}
