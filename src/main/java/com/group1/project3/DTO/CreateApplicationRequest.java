package com.group1.project3.DTO;

import java.util.UUID;

public record CreateApplicationRequest(
        UUID userId,
        UUID projectId,
        UUID roleId,
        String status
) {}
