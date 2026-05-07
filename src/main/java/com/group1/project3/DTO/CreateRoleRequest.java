package com.group1.project3.DTO;

import java.util.UUID;

public record CreateRoleRequest(
        UUID projectId,
        String name
) {}
