package com.egneese.sellers.dto;

/**
 * Created by adityaagrawal on 17/01/16.
 */
public class TransactionDTO {
    private String senderId;
    private String receiverId;
    private String created;
    private String lastUpdated;
    private String status;
    private Double amount;
    private String transactionId;
    private String receiverName;
    private String senderName;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", created='" + created + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", transactionId='" + transactionId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", senderName='" + senderName + '\'' +
                '}';
    }
}
