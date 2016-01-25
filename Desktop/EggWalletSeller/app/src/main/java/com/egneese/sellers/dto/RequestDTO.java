package com.egneese.sellers.dto;

/**
 * Created by nazianoorani on 20/01/16.
 */
public class RequestDTO {

    private String avatarCode;
    private String name;
    private String mobile;
    private String mobileOtp;
    private String email;
    private String id;
    private String city;
    private String password;
    private String referralCode;
    private String created;
    private String gcmKey;
    private String accessToken;
    private String dob;
    private String gender;
    private String lastUpdated;
    private String realm;
    private String scope;
    private String address;
    private String requestId;
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGcmKey() {
        return gcmKey;
    }

    public void setGcmKey(String gcmKey) {
        this.gcmKey = gcmKey;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getMobileOtp() {
        return mobileOtp;
    }

    public void setMobileOtp(String mobileOtp) {
        this.mobileOtp = mobileOtp;
    }

    public String getAvatarCode() {
        return avatarCode;
    }

    public void setAvatarCode(String avatarCode) {
        this.avatarCode = avatarCode;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
