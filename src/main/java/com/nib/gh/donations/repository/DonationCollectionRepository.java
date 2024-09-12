package com.nib.gh.donations.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.nib.gh.donations.model.DonationCollection;

@Repository
public interface DonationCollectionRepository extends JpaRepository<DonationCollection, Long> {

    Optional<DonationCollection> findAllByReceiptNum(String receiptNum);

    List<DonationCollection> findAllByWhoIsEntering(String whoIsEntering);
    
    @Transactional
    Long deleteByReceiptNum(String receiptNum);

    @Query("SELECT COUNT(*) FROM DonationCollection dc WHERE EXISTS (SELECT 1 FROM dc.donationDetails dd WHERE dd.enabled = true)")
    long count();
  
    void deleteById(@SuppressWarnings("null") Long id);

    @Query("SELECT SUM(dc.amount) FROM DonationCollection dc WHERE dc.currency = :currency")
    String sumAmountByCurrency(@Param("currency") String currency);

    @Query("SELECT SUM(dc.amount) FROM DonationCollection dc WHERE dc.currency = :currency AND dc.whoIsEntering = :whoIsEntering AND EXISTS (SELECT 1 FROM dc.donationDetails dd WHERE dd.enabled = true)")
    String sumAmountInGHSForWhoseEntering(@Param("whoIsEntering") String whoseEntering, @Param("currency") String currency);

    @Query("SELECT SUM(dc.amount) FROM DonationCollection dc WHERE dc.currency = :currency AND EXISTS (SELECT 1 FROM dc.donationDetails dd WHERE dd.enabled = true)")
    // @Query("SELECT SUM(dc.amount) FROM DonationCollection dc WHERE dc.currency = :currency AND dc.donation_details_id = :donation_details_id")
    String sumAmountByCurrencyAndDetails(@Param("currency") String currency);

}
