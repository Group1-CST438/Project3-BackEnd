package com.group1.project3.DTO;

import java.util.UUID;

public record GetUserProfileResponseRequest(UUID userProfileId, UUID userId, String bio, String profilePictureUrl ) {
}
