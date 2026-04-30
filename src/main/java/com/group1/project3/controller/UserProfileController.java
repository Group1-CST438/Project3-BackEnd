package com.group1.project3.controller;

import com.group1.project3.DTO.GetUserProfileResponseRequest;
import com.group1.project3.DTO.UpdateUserProfileRequest;
import com.group1.project3.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService){ this.userProfileService = userProfileService;}

    @GetMapping("/user/{userId}")
    public ResponseEntity<GetUserProfileResponseRequest> getProfileByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(userProfileService.getProfileById(userId));
    }

    @PatchMapping("/{profileId}")
    public ResponseEntity<Void> updateProfile(@PathVariable UUID profileId, @Valid @RequestBody UpdateUserProfileRequest request){
        userProfileService.updateProfile(profileId, request);
        return ResponseEntity.noContent().build();
    }
}
