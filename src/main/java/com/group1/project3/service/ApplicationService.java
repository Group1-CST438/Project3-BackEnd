package com.group1.project3.service;

import com.group1.project3.DTO.CreateApplicationRequest;
import com.group1.project3.DTO.GetApplicationResponseRequest;
import com.group1.project3.DTO.UpdateApplicationRequest;
import com.group1.project3.entity.Application;
import com.group1.project3.entity.Project;
import com.group1.project3.entity.Role;
import com.group1.project3.entity.User;
import com.group1.project3.repository.ApplicationRepository;
import com.group1.project3.repository.ProjectRepository;
import com.group1.project3.repository.RoleRepository;
import com.group1.project3.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository,
                              ProjectRepository projectRepository,
                              RoleRepository roleRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.roleRepository = roleRepository;
    }

    public List<GetApplicationResponseRequest> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GetApplicationResponseRequest getApplicationById(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        return toResponse(application);
    }

    public List<GetApplicationResponseRequest> getApplicationsByUserId(UUID userId) {
        return applicationRepository.findByUser_Id(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<GetApplicationResponseRequest> getApplicationsByProjectId(UUID projectId) {
        return applicationRepository.findByProject_Id(projectId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GetApplicationResponseRequest createApplication(CreateApplicationRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        Application application = new Application(null, user, project, role, request.status());
        Application saved = applicationRepository.save(application);
        return toResponse(saved);
    }

    public GetApplicationResponseRequest updateApplication(UUID applicationId, UpdateApplicationRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        if (request.status() != null) application.setStatus(request.status());

        Application saved = applicationRepository.save(application);
        return toResponse(saved);
    }

    public void deleteApplication(UUID applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
        applicationRepository.deleteById(applicationId);
    }

    private GetApplicationResponseRequest toResponse(Application application) {
        return new GetApplicationResponseRequest(
                application.getId(),
                application.getUserId(),
                application.getProjectId(),
                application.getRoleId(),
                application.getStatus()
        );
    }
}
