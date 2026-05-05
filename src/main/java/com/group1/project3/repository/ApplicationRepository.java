package com.group1.project3.repository;

import com.group1.project3.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    @Query("select a from Application a where a.user.id = :userId")
    List<Application> findByUserId(@Param("userId") UUID userId);

    @Query("select a from Application a where a.project.id = :projectId")
    List<Application> findByProjectId(@Param("projectId") UUID projectId);

    List<Application> findByStatus(String status);
}
