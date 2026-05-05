package com.group1.project3.repository;

import com.group1.project3.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("select p from Project p where p.user.id = :userId")
    List<Project> findByUserId(@Param("userId") UUID userId);

    List<Project> findByType(String type);
    List<Project> findByCounty(String county);
}
