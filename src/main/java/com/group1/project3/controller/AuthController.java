package com.group1.project3.controller;

import com.group1.project3.entity.User;
import com.group1.project3.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/api/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User user) {
        User dbUser = userService.getFromOAuth(user);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("username", dbUser.getUsername());
        response.put("name", user.getAttribute("name"));
        response.put("login", user.getAttribute("login"));
        response.put("email", user.getAttribute("email"));
        response.put("oauthProvider", dbUser.getOAuthProvider());
        response.put("role", dbUser.getPermission().name());
        response.put("authorities", user.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .sorted()
                .collect(Collectors.toList()));
        response.put("attributes", user.getAttributes());
        return response;
    }

    @DeleteMapping("/api/me")
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal OAuth2User user){
        User dbUser = userService.getFromOAuth(user);
        userService.delete(dbUser.getId());
        return ResponseEntity.noContent().build();
    }
}
