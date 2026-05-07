package com.group1.project3.repository;

import com.group1.project3.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    List<Application> findByUser_Id(UUID userId);
    List<Application> findByProject_Id(UUID projectId);
    List<Application> findByStatus(String status);
}
