package com.group1.project3.controller;

import com.group1.project3.DTO.CreateApplicationRequest;
import com.group1.project3.DTO.GetApplicationResponseRequest;
import com.group1.project3.DTO.UpdateApplicationRequest;
import com.group1.project3.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<GetApplicationResponseRequest>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<GetApplicationResponseRequest> getApplicationById(@PathVariable UUID applicationId) {
        return ResponseEntity.ok(applicationService.getApplicationById(applicationId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GetApplicationResponseRequest>> getApplicationsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(applicationService.getApplicationsByUserId(userId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<GetApplicationResponseRequest>> getApplicationsByProjectId(@PathVariable UUID projectId) {
        return ResponseEntity.ok(applicationService.getApplicationsByProjectId(projectId));
    }

    @PostMapping
    public ResponseEntity<GetApplicationResponseRequest> createApplication(@Valid @RequestBody CreateApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(request));
    }

    @PatchMapping("/{applicationId}")
    public ResponseEntity<GetApplicationResponseRequest> updateApplication(@PathVariable UUID applicationId, @Valid @RequestBody UpdateApplicationRequest request) {
        return ResponseEntity.ok(applicationService.updateApplication(applicationId, request));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable UUID applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseEntity.noContent().build();
    }
}
