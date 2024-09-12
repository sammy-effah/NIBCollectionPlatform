package com.nib.gh.donations.model;

import lombok.Data;

@Data
public class DonationDetailsObj {
    private String name;
    private String poster;
    private String purpose;
    private boolean enabled;

}
