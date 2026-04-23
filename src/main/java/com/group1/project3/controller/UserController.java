package com.group1.project3.controller;

import com.group1.project3.DTO.GetUserResponseRequest;
import com.group1.project3.DTO.UpdateUserAccountRequest;
import com.group1.project3.DTO.UpdateUserPermissionRequest;
import com.group1.project3.entity.User;
import com.group1.project3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController( UserService userService) {this.userService= userService;}

    @GetMapping
    public ResponseEntity<List<GetUserResponseRequest>> getAllUsers() { return ResponseEntity.ok(userService.getAllUsers());}

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponseRequest> getUserById(@PathVariable UUID userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/{userId}/info")
    public ResponseEntity<Void> updateUserInfo(@PathVariable UUID userId, @Valid @RequestBody UpdateUserAccountRequest request){
        userService.updateInfo(userId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/permission")
    public ResponseEntity<Void> updateUserPermission(@PathVariable UUID userId, @Valid @RequestBody UpdateUserPermissionRequest request){
        userService.updatePermission(userId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId){
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
