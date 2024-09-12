package com.nib.gh.donations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nib.gh.donations.model.Users;
import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    long count();

    boolean existsByUsernameAndEnabled(String username, boolean enabled);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " +
           "FROM Users u JOIN u.roles r " +
           "WHERE u.username = :username AND r.name = 'ADMIN'")
    boolean hasAdminRole(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.enabled = :enabled WHERE u.id = :userId")
    void updateDataById(@Param("userId") Long userId, @Param("enabled") boolean enabled);
}
