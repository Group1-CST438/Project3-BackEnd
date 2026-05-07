package com.group1.project3.service;

import com.group1.project3.DTO.CreateProjectRequest;
import com.group1.project3.DTO.GetProjectResponseRequest;
import com.group1.project3.DTO.UpdateProjectRequest;
import com.group1.project3.entity.Project;
import com.group1.project3.entity.User;
import com.group1.project3.repository.ProjectRepository;
import com.group1.project3.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<GetProjectResponseRequest> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GetProjectResponseRequest getProjectById(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return toResponse(project);
    }

    public List<GetProjectResponseRequest> getProjectsByUserId(UUID userId) {
        return projectRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GetProjectResponseRequest createProject(CreateProjectRequest request) {
        User user = null;
        if (request.userId() != null) {
            user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        }

        Project project = new Project(null, user, request.title(), request.generalDescription(), request.type(), request.county());
        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }

    public GetProjectResponseRequest updateProject(UUID projectId, UpdateProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (request.title() != null) project.setTitle(request.title());
        if (request.generalDescription() != null) project.setGeneralDescription(request.generalDescription());
        if (request.type() != null) project.setType(request.type());
        if (request.county() != null) project.setCounty(request.county());

        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }

    public void deleteProject(UUID projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        projectRepository.deleteById(projectId);
    }

    private GetProjectResponseRequest toResponse(Project project) {
        return new GetProjectResponseRequest(
                project.getId(),
                project.getUserId(),
                project.getTitle(),
                project.getGeneralDescription(),
                project.getType(),
                project.getCounty()
        );
    }

}
