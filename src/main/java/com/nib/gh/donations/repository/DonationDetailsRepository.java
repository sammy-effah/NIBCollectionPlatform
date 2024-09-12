package com.nib.gh.donations.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nib.gh.donations.model.DonationDetails;
import jakarta.transaction.Transactional;

@Repository
public interface DonationDetailsRepository extends JpaRepository<DonationDetails, Long> {

    Optional<DonationDetails> findById(long id);

    List<DonationDetails> findAllByEnabled(boolean enable);

    @Query(value = "SELECT f FROM DonationDetails f ORDER BY f.id DESC LIMIT 1")
    DonationDetails findTopByOrderByIdDesc();

    @Query("SELECT f FROM DonationDetails f WHERE f.date > :timestamp AND f.enabled = TRUE")
    List<DonationDetails> findLatestDonationDetailsForSelect(@Param("timestamp") Timestamp timestamp);

    @Modifying
    @Transactional
    @Query("UPDATE DonationDetails f SET f.enabled = :enabled WHERE f.id = :detailsId")
    void updateDataById(@Param("detailsId") Long detailsId, @Param("enabled") boolean enabled);
}
