package com.group1.project3.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="permission")
    private Permission permission;

    @Column(name = "oauth_provider")
    private String oAuthProvider;

    @Column(name = "oauth_subject")
    private String oauthSubject;

    public User() {
    }

    public User(UUID id, String email, String username, String password, String oAuthProvider) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.oAuthProvider = oAuthProvider;
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
        return oAuthProvider;
    }

    public void setOAuthProvider(String oAuthProvider) {
        this.oAuthProvider = oAuthProvider;
    }

    public String getOauthSubject() { return oauthSubject; }

    public void setOauthSubject( String oauthSubject) {this.oauthSubject = oauthSubject; }


}
