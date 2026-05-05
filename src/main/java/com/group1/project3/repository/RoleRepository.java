package com.group1.project3.repository;

import com.group1.project3.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("select r from Role r where r.id = :roleId and r.project.id = :projectId")
    Optional<Role> findByIdAndProjectId(@Param("roleId") UUID roleId, @Param("projectId") UUID projectId);
}
