package com.group1.project3.entity;

import jakarta.persistence.*;
import org.springframework.security.core.parameters.P;

import java.util.UUID;

@Entity
@Table(name = "user_projects")
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
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

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Project getProject() {return project;}

    public void setProject(Project project) {this.project = project;}

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}


}
