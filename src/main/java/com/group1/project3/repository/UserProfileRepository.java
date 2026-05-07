package com.group1.project3.repository;

import com.group1.project3.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    @Query("select up from UserProfile up where up.user.id = :userId")
    Optional<UserProfile> findByUserId(@Param("userId") UUID userId);
}
