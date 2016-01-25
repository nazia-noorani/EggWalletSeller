package com.egneese.sellers.dto;

/**
 * Created by nazianoorani on 20/01/16.
 **/

public class SessionDTO {
    private String gcmID;
    private UserxDTO userxDTO;
    private SellerDTO sellerDTO;


    public SellerDTO getSellerDTO() {
        return sellerDTO;
    }

    public void setSellerDTO(SellerDTO sellerDTO) {
        this.sellerDTO = sellerDTO;
    }

    public UserxDTO getUserxDTO() {
        return userxDTO;
    }

    public void setUserxDTO(UserxDTO userxDTO) {
        this.userxDTO = userxDTO;
    }

    public String getGcmID() {
        return gcmID;
    }

    public void setGcmID(String gcmID) {
        this.gcmID = gcmID;
    }

    @Override
    public String toString() {
        return "SessionDTO{" +
                "gcmID='" + gcmID + '\'' +
                ", sellerDTO=" + sellerDTO.toString() +
                '}';
    }
}
