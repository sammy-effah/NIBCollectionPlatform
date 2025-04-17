package com.nib.gh.donations.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.nib.gh.donations.model.DonationCollection;
import com.nib.gh.donations.model.DonationDetails;

@Repository
public interface DonationCollectionRepository extends JpaRepository<DonationCollection, Long> {

    Optional<DonationCollection> findAllByReceiptNum(String receiptNum);

    List<DonationCollection> findAllByDonationDetails(DonationDetails donationDetails);

    List<DonationCollection> findAllByWhoIsEnteringAndDonationDetails(String whoIsEntering, DonationDetails donationDetails);
    
    @Transactional
    Long deleteByReceiptNum(String receiptNum);

    @Query("SELECT COUNT(*) FROM DonationCollection dc WHERE EXISTS (SELECT 1 FROM dc.donationDetails dd WHERE dd.id = :donation_details_id)")
    long count(@Param("donation_details_id") Long donation_details_id);
  
    void deleteById(@SuppressWarnings("null") Long id);

    @Query("SELECT SUM(dc.amount) FROM DonationCollection dc WHERE dc.currency = :currency AND dc.donationDetails.id = :donation_details_id")
    String sumAmountByCurrency(@Param("currency") String currency, @Param("donation_details_id") Long donation_details_id);
    // String sumAmountByCurrency(@Param("currency") String currency);

    @Query("SELECT SUM(dc.amount) FROM DonationCollection dc WHERE dc.currency = :currency AND dc.whoIsEntering = :whoIsEntering AND EXISTS (SELECT 1 FROM dc.donationDetails dd WHERE dd.id = :donation_details_id)")
    String sumAmountInGHSForWhoseEntering(@Param("whoIsEntering") String whoseEntering, @Param("currency") String currency, @Param("donation_details_id") Long donation_details_id);

    @Query("SELECT SUM(dc.amount) FROM DonationCollection dc WHERE dc.currency = :currency AND EXISTS (SELECT 1 FROM dc.donationDetails dd WHERE dd.id = :donation_details_id)")
    // @Query("SELECT SUM(dc.amount) FROM DonationCollection dc WHERE dc.currency = :currency AND dc.donation_details_id = :donation_details_id")
    String sumAmountByCurrencyAndDetails(@Param("currency") String currency, @Param("donation_details_id") Long donation_details_id);

}
