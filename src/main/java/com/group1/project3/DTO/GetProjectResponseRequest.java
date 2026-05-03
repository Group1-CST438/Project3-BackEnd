package com.group1.project3.DTO;

import java.util.UUID;

public record GetProjectResponseRequest(
        UUID id,
        UUID userId,
        String title,
        String generalDescription,
        String type,
        String county
) {}
