package com.group1.project3.service;

import com.group1.project3.DTO.CreateUserProjectRequest;
import com.group1.project3.DTO.GetUserProjectResponseRequest;
import com.group1.project3.DTO.UpdateUserProjectRequest;
import com.group1.project3.entity.Project;
import com.group1.project3.entity.Role;
import com.group1.project3.entity.User;
import com.group1.project3.entity.UserProject;
import com.group1.project3.repository.ProjectRepository;
import com.group1.project3.repository.RoleRepository;
import com.group1.project3.repository.UserProjectRepository;
import com.group1.project3.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;

    public UserProjectService(UserProjectRepository userProjectRepository,
                              UserRepository userRepository,
                              ProjectRepository projectRepository,
                              RoleRepository roleRepository) {
        this.userProjectRepository = userProjectRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.roleRepository = roleRepository;
    }

    public List<GetUserProjectResponseRequest> getAllUserProjects() {
        return userProjectRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<GetUserProjectResponseRequest> getProjectsByUserId(UUID userId) {
        return userProjectRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GetUserProjectResponseRequest getProjectByUserIdAndProjectId(UUID userId, UUID projectId) {
        UserProject userProject = userProjectRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User project assignment not found"));
        return toResponse(userProject);
    }

    public GetUserProjectResponseRequest assignProjectToUser(CreateUserProjectRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Role role = roleRepository.findByIdAndProjectId(request.roleId(), request.projectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found for project"));

        if (userProjectRepository.existsByUserIdAndProjectId(request.userId(), request.projectId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project is already assigned to user");
        }

        UserProject userProject = new UserProject(null, user, project, role);
        UserProject saved = userProjectRepository.save(userProject);
        return toResponse(saved);
    }

    public GetUserProjectResponseRequest updateUserProjectRole(UUID userId, UUID projectId, UpdateUserProjectRequest request) {
        UserProject userProject = userProjectRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User project assignment not found"));

        Role role = roleRepository.findByIdAndProjectId(request.roleId(), projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found for project"));

        userProject.setRole(role);
        UserProject saved = userProjectRepository.save(userProject);
        return toResponse(saved);
    }

    public void unassignProjectFromUser(UUID userId, UUID projectId) {
        UserProject userProject = userProjectRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User project assignment not found"));

        userProjectRepository.delete(userProject);
    }

    private GetUserProjectResponseRequest toResponse(UserProject userProject) {
        return new GetUserProjectResponseRequest(
                userProject.getId(),
                userProject.getUserId(),
                userProject.getProjectId(),
                userProject.getRoleId()
        );
    }
}
