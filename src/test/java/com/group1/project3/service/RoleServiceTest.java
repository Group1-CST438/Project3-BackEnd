package com.group1.project3.service;

import com.group1.project3.DTO.CreateRoleRequest;
import com.group1.project3.DTO.GetRoleResponseRequest;
import com.group1.project3.DTO.UpdateRoleRequest;
import com.group1.project3.entity.Project;
import com.group1.project3.entity.Role;
import com.group1.project3.entity.User;
import com.group1.project3.repository.ProjectRepository;
import com.group1.project3.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private RoleService roleService;

    private User testUser;
    private Project testProject;
    private Role testRole;
    private UUID userId;
    private UUID projectId;
    private UUID roleId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        roleId = UUID.randomUUID();

        testUser = new User(userId, "test@email.com", "testuser", "password", null, null);
        testProject = new Project(projectId, testUser, "Test Project", "Desc", "Web", "Monterey");
        testRole = new Role(roleId, "Developer", testProject);
    }

    @Test
    void getRolesByProjectId_returnsList() {
        when(roleRepository.findByProjectId(projectId)).thenReturn(List.of(testRole));

        List<GetRoleResponseRequest> result = roleService.getRolesByProjectId(projectId);

        assertEquals(1, result.size());
        assertEquals("Developer", result.get(0).name());
        assertEquals(projectId, result.get(0).projectId());
        verify(roleRepository).findByProjectId(projectId);
    }

    @Test
    void getRolesByProjectId_returnsEmptyList() {
        when(roleRepository.findByProjectId(projectId)).thenReturn(List.of());

        List<GetRoleResponseRequest> result = roleService.getRolesByProjectId(projectId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRoleById_returnsRole() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(testRole));

        GetRoleResponseRequest result = roleService.getRoleById(roleId);

        assertEquals(roleId, result.id());
        assertEquals("Developer", result.name());
        assertEquals(projectId, result.projectId());
    }

    @Test
    void getRoleById_throwsWhenNotFound() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> roleService.getRoleById(roleId));
    }

    @Test
    void createRole_createsSuccessfully() {
        CreateRoleRequest request = new CreateRoleRequest(projectId, "Designer");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
            Role saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        GetRoleResponseRequest result = roleService.createRole(request);

        assertNotNull(result);
        assertEquals("Designer", result.name());
        assertEquals(projectId, result.projectId());
        verify(projectRepository).findById(projectId);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void createRole_throwsWhenProjectNotFound() {
        CreateRoleRequest request = new CreateRoleRequest(projectId, "Designer");

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> roleService.createRole(request));
        verify(roleRepository, never()).save(any());
    }

    @Test
    void updateRole_updatesName() {
        UpdateRoleRequest request = new UpdateRoleRequest("Lead Developer");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(testRole));
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        GetRoleResponseRequest result = roleService.updateRole(roleId, request);

        assertEquals("Lead Developer", result.name());
    }

    @Test
    void updateRole_throwsWhenNotFound() {
        UpdateRoleRequest request = new UpdateRoleRequest("Lead Developer");

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> roleService.updateRole(roleId, request));
    }

    @Test
    void deleteRole_deletesSuccessfully() {
        when(roleRepository.existsById(roleId)).thenReturn(true);

        roleService.deleteRole(roleId);

        verify(roleRepository).deleteById(roleId);
    }

    @Test
    void deleteRole_throwsWhenNotFound() {
        when(roleRepository.existsById(roleId)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> roleService.deleteRole(roleId));
        verify(roleRepository, never()).deleteById(any());
    }
}
