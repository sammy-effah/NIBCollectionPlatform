package com.nib.gh.donations.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@Data
public class InstitutionPayCollections {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String voucherNumber;
    private String name;
    private String indexNumber;
    private String level;
    private String phoneNumber;
    private Double amount;
    private LocalDateTime createDate;
    private String enteredBy;
    private String purpose;

    
    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

    @ManyToOne(cascade = CascadeType.REMOVE) // Consider cascade type based on your needs
    @JoinColumn(name = "institution_id", referencedColumnName = "id")
    private Institution institution;

}
