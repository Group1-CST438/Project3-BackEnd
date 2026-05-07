package com.group1.project3.DTO;

import java.util.UUID;

public record GetRoleResponseRequest(
        UUID id,
        UUID projectId,
        String name
) {}
