package com.group1.project3.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_projects")
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public UserProject() {
    }

    public UserProject(UUID id, User user, Project project, Role role) {
        this.id = id;
        this.user = user;
        this.project = project;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() { return user.getId(); }

    public void setUserId(UUID userId) { user.setId(userId); }

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public UUID getProjectId() { return project.getId(); }

    public void setProjectId(UUID projectId) { project.setId(projectId); }

    public Project getProject() {return project;}

    public void setProject(Project project) {this.project = project;}

    public UUID getRoleId() { return role.getId(); }

    public void setRoleId(UUID roleId) { role.setId(roleId); }

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}


}
