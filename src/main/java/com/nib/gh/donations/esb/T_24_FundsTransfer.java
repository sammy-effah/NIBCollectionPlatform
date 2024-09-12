package com.nib.gh.donations.esb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "t_24_funds_transfer", schema = "esb") // Adjust schema if necessary
// t_24_funds_transfer
public class T_24_FundsTransfer {

 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String creditAccount;
    private String currency;
    private String debitAccount;
    private String elevyCharge;
    private String statusReason;
    private String timeStamp;
    private double totalCharges;
    private double transactionAmount;
    private String transactionDescription;
    private String transactionReference;
    private String transactionSource;
    private String transactionStatus;
    private String uniqueReference;
    private String valueDate;
    private String ftType;
    private String recordStatus;
    private String requestId;

}
