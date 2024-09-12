package com.nib.gh.donations.model;

import java.time.LocalDateTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
// import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
// @Table(name = "donation_collection", schema = "funeral_collections")
public class DonationCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contributorName;
    private String phone;
    private String description;
    private String beneficiary;
    private LocalDateTime dateData;
    private double amount;
    private String whoIsEntering;
    private String itemsAndQuantity;
    private String receiptNum;
    private String currency;
    private String cheque;

    @PrePersist
    protected void onCreate() {
        dateData = LocalDateTime.now();
    }

    @OneToOne(cascade = CascadeType.REMOVE) // Consider cascade type based on your needs
    @JoinColumn(name = "donation_details_id", referencedColumnName = "id")
    private DonationDetails donationDetails;
}
