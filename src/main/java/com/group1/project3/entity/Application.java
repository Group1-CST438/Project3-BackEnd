package com.group1.project3.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    @JoinColumn(name="project_id", nullable = false)
    private Project project;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name="status", nullable = false)
    private String status;

    public Application() {
    }

    public Application(UUID id, User user, Project project, Role role, String status) {
        this.id = id;
        this.user = user;
        this.project = project;
        this.role = role;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return user.getId();
    }

    public void setUserId(UUID userId) {
        user.setId(userId);
    }

    public UUID getProjectId() {
        return project.getId();
    }

    public void setProjectId(UUID projectId) {
        project.setId(projectId);
    }

    public UUID getRoleId(){ return role.getId();}

    public void setRoleId(UUID roleId) { role.setId(roleId);}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
