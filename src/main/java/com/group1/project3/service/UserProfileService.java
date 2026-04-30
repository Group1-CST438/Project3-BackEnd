package com.group1.project3.service;

import com.group1.project3.DTO.GetUserProfileResponseRequest;
import com.group1.project3.DTO.UpdateUserProfileRequest;
import com.group1.project3.entity.User;
import com.group1.project3.entity.UserProfile;
import com.group1.project3.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository){ this.userProfileRepository = userProfileRepository;}

    public GetUserProfileResponseRequest getProfileById(UUID userId){
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        return new GetUserProfileResponseRequest(
                profile.getId(),
                profile.getUserId(),
                profile.getBio(),
                profile.getProfilePictureUrl()
        );
    }

    public UserProfile create(User user){
        UserProfile newProfile = new UserProfile();

        newProfile.setUser(user);
        newProfile.setBio("");
        newProfile.setProfilePictureUrl("");

        return userProfileRepository.save(newProfile);
    }

    public UserProfile updateProfile(UUID profileId, UpdateUserProfileRequest request){
        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile does not exist"));

        if (request.bio() != null) profile.setBio(request.bio());
        if(request.profilePictureUrl() != null) profile.setProfilePictureUrl(request.profilePictureUrl());

        return userProfileRepository.save(profile);
    }


    public UserProfile delete(UUID profileId){
        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        userProfileRepository.delete(profile);
        return profile;
    }
}
