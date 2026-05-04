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
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private User testUser;
    private Project testProject;
    private Role testRole;
    private Application testApplication;
    private UUID userId;
    private UUID projectId;
    private UUID roleId;
    private UUID applicationId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        roleId = UUID.randomUUID();
        applicationId = UUID.randomUUID();

        testUser = new User(userId, "test@email.com", "testuser", "password", null, null);
        testProject = new Project(projectId, testUser, "Test Project", "Desc", "Web", "Monterey");
        testRole = new Role(roleId, "Developer");
        testApplication = new Application(applicationId, testUser, testProject, testRole, "PENDING");
    }

    @Test
    void getAllApplications_returnsList() {
        when(applicationRepository.findAll()).thenReturn(List.of(testApplication));

        List<GetApplicationResponseRequest> result = applicationService.getAllApplications();

        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).status());
        verify(applicationRepository).findAll();
    }

    @Test
    void getAllApplications_returnsEmptyList() {
        when(applicationRepository.findAll()).thenReturn(List.of());

        List<GetApplicationResponseRequest> result = applicationService.getAllApplications();

        assertTrue(result.isEmpty());
    }

    @Test
    void getApplicationById_returnsApplication() {
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(testApplication));

        GetApplicationResponseRequest result = applicationService.getApplicationById(applicationId);

        assertEquals(applicationId, result.id());
        assertEquals("PENDING", result.status());
    }

    @Test
    void getApplicationById_throwsWhenNotFound() {
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> applicationService.getApplicationById(applicationId));
    }

    @Test
    void getApplicationsByUserId_returnsList() {
        when(applicationRepository.findByUserId(userId)).thenReturn(List.of(testApplication));

        List<GetApplicationResponseRequest> result = applicationService.getApplicationsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).userId());
    }

    @Test
    void getApplicationsByProjectId_returnsList() {
        when(applicationRepository.findByProjectId(projectId)).thenReturn(List.of(testApplication));

        List<GetApplicationResponseRequest> result = applicationService.getApplicationsByProjectId(projectId);

        assertEquals(1, result.size());
        assertEquals(projectId, result.get(0).projectId());
    }

    @Test
    void createApplication_createsSuccessfully() {
        CreateApplicationRequest request = new CreateApplicationRequest(userId, projectId, roleId, "PENDING");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(testRole));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> {
            Application saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        GetApplicationResponseRequest result = applicationService.createApplication(request);

        assertNotNull(result);
        assertEquals("PENDING", result.status());
        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void createApplication_throwsWhenUserNotFound() {
        CreateApplicationRequest request = new CreateApplicationRequest(userId, projectId, roleId, "PENDING");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(request));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void createApplication_throwsWhenProjectNotFound() {
        CreateApplicationRequest request = new CreateApplicationRequest(userId, projectId, roleId, "PENDING");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(request));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void createApplication_throwsWhenRoleNotFound() {
        CreateApplicationRequest request = new CreateApplicationRequest(userId, projectId, roleId, "PENDING");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(request));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void updateApplication_updatesStatus() {
        UpdateApplicationRequest request = new UpdateApplicationRequest("APPROVED");

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        GetApplicationResponseRequest result = applicationService.updateApplication(applicationId, request);

        assertEquals("APPROVED", result.status());
    }

    @Test
    void updateApplication_throwsWhenNotFound() {
        UpdateApplicationRequest request = new UpdateApplicationRequest("APPROVED");

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> applicationService.updateApplication(applicationId, request));
    }

    @Test
    void deleteApplication_deletesSuccessfully() {
        when(applicationRepository.existsById(applicationId)).thenReturn(true);

        applicationService.deleteApplication(applicationId);

        verify(applicationRepository).deleteById(applicationId);
    }

    @Test
    void deleteApplication_throwsWhenNotFound() {
        when(applicationRepository.existsById(applicationId)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> applicationService.deleteApplication(applicationId));
        verify(applicationRepository, never()).deleteById(any());
    }
}
