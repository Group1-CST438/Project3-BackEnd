package com.group1.project3.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateUserProjectRequest(
        @NotNull UUID roleId
) {}
