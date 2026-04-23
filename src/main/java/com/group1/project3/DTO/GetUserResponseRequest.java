package com.group1.project3.DTO;

import com.group1.project3.entity.Permission;

import java.util.UUID;

public record GetUserResponseRequest(UUID userId,String username, String email, Permission permission) {
}
