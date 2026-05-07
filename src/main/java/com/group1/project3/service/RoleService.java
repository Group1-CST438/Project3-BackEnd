package com.group1.project3.service;

import com.group1.project3.DTO.CreateRoleRequest;
import com.group1.project3.DTO.GetRoleResponseRequest;
import com.group1.project3.DTO.UpdateRoleRequest;
import com.group1.project3.entity.Project;
import com.group1.project3.entity.Role;
import com.group1.project3.repository.ProjectRepository;
import com.group1.project3.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;

    public RoleService(RoleRepository roleRepository, ProjectRepository projectRepository) {
        this.roleRepository = roleRepository;
        this.projectRepository = projectRepository;
    }

    public List<GetRoleResponseRequest> getRolesByProjectId(UUID projectId) {
        return roleRepository.findByProjectId(projectId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GetRoleResponseRequest getRoleById(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        return toResponse(role);
    }

    public GetRoleResponseRequest createRole(CreateRoleRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Role role = new Role(null, request.name(), project);
        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    public GetRoleResponseRequest updateRole(UUID roleId, UpdateRoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        if (request.name() != null) role.setName(request.name());

        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    public void deleteRole(UUID roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        roleRepository.deleteById(roleId);
    }

    private GetRoleResponseRequest toResponse(Role role) {
        return new GetRoleResponseRequest(
                role.getId(),
                role.getProjectId(),
                role.getName()
        );
    }
}
