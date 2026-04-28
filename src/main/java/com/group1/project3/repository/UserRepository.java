package com.group1.project3.repository;

import com.group1.project3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByAuthProviderAndAuthSubject(String oauthProvider, String oauthSubject);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
