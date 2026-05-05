package com.group1.project3.repository;

import com.group1.project3.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, UUID> {
    @Query("select up from UserProject up where up.user.id = :userId")
    List<UserProject> findByUserId(@Param("userId") UUID userId);

    @Query("select up from UserProject up where up.user.id = :userId and up.project.id = :projectId")
    Optional<UserProject> findByUserIdAndProjectId(@Param("userId") UUID userId, @Param("projectId") UUID projectId);

    @Query("select count(up) > 0 from UserProject up where up.user.id = :userId and up.project.id = :projectId")
    boolean existsByUserIdAndProjectId(@Param("userId") UUID userId, @Param("projectId") UUID projectId);
}
