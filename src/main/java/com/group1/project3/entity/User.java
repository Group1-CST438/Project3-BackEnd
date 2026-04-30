package com.group1.project3.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="permission")
    private Permission permission;

    @Column(name = "oauth_provider")
    private String authProvider;

    @Column(name = "oauth_subject")
    private String authSubject;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projectEntries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;

    public User() {
    }

    public User(UUID id, String email, String username, String password, String authProvider, String authSubject) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.authProvider = authProvider;
        this.authSubject = authSubject;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOAuthProvider() {
        return authProvider;
    }

    public void setOAuthProvider(String oAuthProvider) {
        this.authProvider = oAuthProvider;
    }

    public String getOauthSubject() { return authSubject; }

    public void setOauthSubject( String oauthSubject) {this.authSubject = oauthSubject; }

    public Permission getPermission() {return permission;}

    public void setPermission( Permission permission) { this.permission = permission;}

    public UserProfile getProfile() { return profile;}

    public void setProfile(UserProfile profile){
        this.profile = profile;
        if(profile != null){
            profile.setUser(this);
        }
    }


}
