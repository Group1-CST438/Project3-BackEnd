package com.group1.project3.DTO;

public record UpdateProjectRequest(
        String title,
        String generalDescription,
        String type,
        String county
) {}
