package com.nib.gh.donations.model;

import lombok.Data;

@Data
public class SMSRequest {
    private String action;
    private String api_key;
    private String to;
    private String from;
    private String sms;
}
