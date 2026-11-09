package com.nib.gh.donations.model;

import lombok.Data;

@Data
public class InstitutionPayObj {

    private Double amount;
    private String enteredBy;
    private String indexNumber;
    private String institution;
    private String level;
    private String name;
    private String phoneNumber;
    private String purpose;
    private String voucherNumber;

}
