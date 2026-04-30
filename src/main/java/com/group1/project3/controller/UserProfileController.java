package com.group1.project3.controller;

import com.group1.project3.DTO.GetUserProfileResponseRequest;
import com.group1.project3.service.UserProfileService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService){ this.userProfileService = userProfileService;}

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserProfileResponseRequest> getProfileByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(userProfileService.getProfileById(userId));
    }

    @PatchMapping("/{profileId}")


}
