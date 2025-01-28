package com.nib.gh.donations.model;

import lombok.Data;

@Data
public class DonationDetailsObj {
    private Long id;
    private String nameD;
    private String poster;
    private String purpose;
    private boolean enabled;

}
