package com.nib.gh.donations.model;

import java.sql.Timestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
// @Table(name = "donation_details", schema = "funeral_collections")
public class DonationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private String picLocation;
    private String purpose;
    private boolean enabled;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;
}
