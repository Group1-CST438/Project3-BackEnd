package com.group1.project3.DTO;

import java.util.UUID;

public record GetUserProjectResponseRequest(
        UUID id,
        UUID userId,
        UUID projectId,
        UUID roleId
) {}
