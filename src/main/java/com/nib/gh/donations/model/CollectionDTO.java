package com.nib.gh.donations.model;

import java.time.LocalDateTime;

public class CollectionDTO {

    private  String contributorName;
    private  String phone;
    private  String description;
    private  String beneficiary;
    private  LocalDateTime dateData;
    private double amount;
    private long whoseDonation;
    private String whoIsEntering;
    private String itemsAndQuantity;
    private String receiptNum;
    private String currency;
    private String cheque;
    
    public String getContributorName() {
        return contributorName;
    }
    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    public LocalDateTime getDateData() {
        return dateData;
    }
    public void setDateData(LocalDateTime dateData) {
        this.dateData = dateData;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public long getWhoseDonation() {
        return whoseDonation;
    }
    public void setWhoseDonation(long whosefuneral) {
        this.whoseDonation = whosefuneral;
    }
    public String getWhoIsEntering() {
        return whoIsEntering;
    }
    public void setWhoIsEntering(String whoisentering) {
        this.whoIsEntering = whoisentering;
    }
    public String getItemsAndQuantity() {
        return itemsAndQuantity;
    }
    public void setItemsAndQuantity(String itemsAndQuantity) {
        this.itemsAndQuantity = itemsAndQuantity;
    }
    public String getReceiptNum() {
        return receiptNum;
    }
    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getCheque() {
        return cheque;
    }
    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

}
