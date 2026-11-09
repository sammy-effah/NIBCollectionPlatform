package com.nib.gh.donations.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nib.gh.donations.model.Institution;
import com.nib.gh.donations.model.InstitutionPayCollections;

public interface InstitutionPayCollectionsRepository extends JpaRepository<InstitutionPayCollections, Long> {

    Optional<InstitutionPayCollections> findAllByVoucherNumber(String receiptNum);

    List<InstitutionPayCollections> findAllByInstitution(Institution institution);

    List<InstitutionPayCollections> findAllByEnteredByAndInstitution(String enteredBy, Institution institution);

    @Transactional
    Long deleteByVoucherNumber(String voucherNumber);

    @Query("SELECT COUNT(*) FROM InstitutionPayCollections ipc WHERE ipc.institution.id = :institution_id")
    long count(@Param("institution_id") Long institution_id);

    @Query("""
                SELECT COUNT(ipc)
                FROM InstitutionPayCollections ipc
                WHERE ipc.institution.id = :institutionId
                  AND ipc.createDate >= :startOfDay
                  AND ipc.createDate < :endOfDay
            """)
    long countToday(
            @Param("institutionId") Long institutionId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    void deleteById(@SuppressWarnings("null") Long id);

    @Query("SELECT SUM(ipc.amount) FROM InstitutionPayCollections ipc WHERE  ipc.enteredBy = :enteredBy AND EXISTS (SELECT 1 FROM ipc.institution i WHERE i.id = :institution_id)")
    String sumAmountInForEnteredBy(@Param("enteredBy") String enteredBy, @Param("institution_id") Long institution_id);

    @Query("""
                SELECT COALESCE(SUM(ipc.amount), 0)
                FROM InstitutionPayCollections ipc
                WHERE ipc.enteredBy = :enteredBy
                  AND ipc.institution.id = :institutionId
                  AND ipc.createDate >= :startOfDay
                  AND ipc.createDate < :endOfDay
            """)
    Double sumAmountInForEnteredByToday(
            @Param("enteredBy") String enteredBy,
            @Param("institutionId") Long institutionId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT SUM(ipc.amount) FROM InstitutionPayCollections ipc WHERE ipc.institution.id = :institution_id")
    BigDecimal sumAmountByInstitution(@Param("institution_id") Long institution_id);

    @Query("""
                SELECT COALESCE(SUM(ipc.amount), 0)
                FROM InstitutionPayCollections ipc
                WHERE ipc.institution.id = :institutionId
                  AND ipc.createDate >= :startOfDay
                  AND ipc.createDate < :endOfDay
            """)
    BigDecimal sumAmountByInstitutionToday(
            @Param("institutionId") Long institutionId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("""
                SELECT ipc
                FROM InstitutionPayCollections ipc
                WHERE ipc.institution = :institution
                  AND ipc.createDate >= :startOfDay
                  AND ipc.createDate < :endOfDay
            """)
    List<InstitutionPayCollections> findTodayCollections(
            @Param("institution") Institution institution,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("""
                SELECT ipc
                FROM InstitutionPayCollections ipc
                WHERE ipc.enteredBy = :enteredBy
                  AND ipc.institution = :institution
                  AND ipc.createDate >= :startOfDay
                  AND ipc.createDate < :endOfDay
            """)
    List<InstitutionPayCollections> findAllForUserForToday(
            @Param("enteredBy") String enteredBy,
            @Param("institution") Institution institution,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

}
