package com.group1.project3.repository;

import com.group1.project3.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByUser_Id(UUID userId);
    List<Project> findByType(String type);
    List<Project> findByCounty(String county);
}
