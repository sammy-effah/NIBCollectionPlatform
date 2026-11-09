package com.nib.gh.donations.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.nib.gh.donations.model.Institution;

import jakarta.transaction.Transactional;

public interface InstitutionRepository extends JpaRepository<Institution, Long>{

     Optional<Institution> findById(long id);

    List<Institution> findAllByEnabled(boolean enable);

    @Query(value = "SELECT inst FROM Institution inst ORDER BY inst.id DESC LIMIT 1")
    Institution findTopByOrderByIdDesc();

    @Query("SELECT inst FROM Institution inst WHERE inst.date > :timestamp AND inst.enabled = TRUE")
    List<Institution> findLatestInstitutionForSelect(@Param("timestamp") Timestamp timestamp);

    @Modifying
    @Transactional
    @Query("UPDATE Institution inst SET inst.enabled = :enabled WHERE inst.id = :id")
    void updateDataById(@Param("id") Long detailsId, @Param("enabled") boolean enabled);
    
}
