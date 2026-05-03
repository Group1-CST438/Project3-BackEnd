package com.group1.project3.controller;

import com.group1.project3.DTO.CreateProjectRequest;
import com.group1.project3.DTO.GetProjectResponseRequest;
import com.group1.project3.DTO.UpdateProjectRequest;
import com.group1.project3.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<GetProjectResponseRequest>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<GetProjectResponseRequest> getProjectById(@PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GetProjectResponseRequest>> getProjectsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(projectService.getProjectsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<GetProjectResponseRequest> createProject(@Valid @RequestBody CreateProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<GetProjectResponseRequest> updateProject(@PathVariable UUID projectId, @Valid @RequestBody UpdateProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(projectId, request));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
