package com.group1.project3.DTO;

import com.group1.project3.entity.Permission;
import jakarta.validation.constraints.NotNull;

public record UpdateUserPermissionRequest(@NotNull Permission permission) {
}
