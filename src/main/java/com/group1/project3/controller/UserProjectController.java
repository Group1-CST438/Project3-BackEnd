package com.group1.project3.controller;

import com.group1.project3.DTO.CreateUserProjectRequest;
import com.group1.project3.DTO.GetUserProjectResponseRequest;
import com.group1.project3.DTO.UpdateUserProjectRequest;
import com.group1.project3.service.UserProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assigned")
public class UserProjectController {

    private final UserProjectService userProjectService;

    public UserProjectController(UserProjectService userProjectService){
        this.userProjectService = userProjectService;
    }

    @GetMapping
    public ResponseEntity<List<GetUserProjectResponseRequest>> getAllUserProjects(){
        return ResponseEntity.ok(userProjectService.getAllUserProjects());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GetUserProjectResponseRequest>> getProjectsByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(userProjectService.getProjectsByUserId(userId));
    }

    @GetMapping("/user/{userId}/project/{projectId}")
    public ResponseEntity<GetUserProjectResponseRequest> getProjectByUserIdAndProjectId(@PathVariable UUID userId, @PathVariable UUID projectId){
        return ResponseEntity.ok(userProjectService.getProjectByUserIdAndProjectId(userId, projectId));
    }

    @PostMapping
    public ResponseEntity<GetUserProjectResponseRequest> assignProjectToUser(@Valid @RequestBody CreateUserProjectRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userProjectService.assignProjectToUser(request));
    }

    @PatchMapping("/user/{userId}/project/{projectId}")
    public ResponseEntity<GetUserProjectResponseRequest> updateUserProjectRole(@PathVariable UUID userId, @PathVariable UUID projectId, @Valid @RequestBody UpdateUserProjectRequest request){
        return ResponseEntity.ok(userProjectService.updateUserProjectRole(userId, projectId, request));
    }

    @DeleteMapping("/user/{userId}/project/{projectId}")
    public ResponseEntity<Void> unassignProjectFromUser(@PathVariable UUID userId, @PathVariable UUID projectId){
        userProjectService.unassignProjectFromUser(userId, projectId);
        return ResponseEntity.noContent().build();
    }

}
