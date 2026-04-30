package com.group1.project3.service;

import com.group1.project3.DTO.GetUserProfileResponseRequest;
import com.group1.project3.entity.User;
import com.group1.project3.entity.UserProfile;
import com.group1.project3.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.html.HTMLTableCaptionElement;

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

        newProfile.setUserId(user.getId());
        newProfile.setBio("");
        newProfile.setProfilePictureUrl("");

        return userProfileRepository.save(newProfile);
    }

}
