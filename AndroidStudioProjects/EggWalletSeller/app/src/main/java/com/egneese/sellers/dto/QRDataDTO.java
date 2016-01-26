package com.egneese.sellers.dto;

/**
 * Created by adityaagrawal on 12/01/16.
 */
public class QRDataDTO {
    private String requestId;
    private String requestType;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "QRDataDTO{" +
                "requestId='" + requestId + '\'' +
                ", requestType='" + requestType + '\'' +
                '}';
    }
}
