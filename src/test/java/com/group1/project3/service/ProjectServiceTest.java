package com.group1.project3.service;

import com.group1.project3.DTO.CreateProjectRequest;
import com.group1.project3.DTO.GetProjectResponseRequest;
import com.group1.project3.DTO.UpdateProjectRequest;
import com.group1.project3.entity.Project;
import com.group1.project3.entity.User;
import com.group1.project3.repository.ProjectRepository;
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
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private User testUser;
    private Project testProject;
    private UUID userId;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        projectId = UUID.randomUUID();

        testUser = new User(userId, "test@email.com", "testuser", "password", null, null);

        testProject = new Project(projectId, testUser, "Test Project", "A test project", "Web App", "Monterey");
    }

    @Test
    void getAllProjects_returnsListOfProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(testProject));

        List<GetProjectResponseRequest> result = projectService.getAllProjects();

        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).title());
        verify(projectRepository).findAll();
    }

    @Test
    void getAllProjects_returnsEmptyList() {
        when(projectRepository.findAll()).thenReturn(List.of());

        List<GetProjectResponseRequest> result = projectService.getAllProjects();

        assertTrue(result.isEmpty());
    }

    @Test
    void getProjectById_returnsProject() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));

        GetProjectResponseRequest result = projectService.getProjectById(projectId);

        assertEquals(projectId, result.id());
        assertEquals("Test Project", result.title());
        assertEquals("Monterey", result.county());
    }

    @Test
    void getProjectById_throwsWhenNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> projectService.getProjectById(projectId));
    }

    @Test
    void getProjectsByUserId_returnsProjects() {
        when(projectRepository.findByUser_Id(userId)).thenReturn(List.of(testProject));

        List<GetProjectResponseRequest> result = projectService.getProjectsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).userId());
    }

    @Test
    void createProject_createsAndReturnsProject() {
        CreateProjectRequest request = new CreateProjectRequest(userId, "New Project", "Description", "Mobile", "Santa Cruz");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        GetProjectResponseRequest result = projectService.createProject(request);

        assertNotNull(result);
        assertEquals("New Project", result.title());
        assertEquals("Mobile", result.type());
        assertEquals("Santa Cruz", result.county());
        verify(userRepository).findById(userId);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void createProject_throwsWhenUserNotFound() {
        CreateProjectRequest request = new CreateProjectRequest(userId, "New Project", "Desc", "Web", "LA");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> projectService.createProject(request));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void updateProject_updatesFields() {
        UpdateProjectRequest request = new UpdateProjectRequest("Updated Title", null, "API", null);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        GetProjectResponseRequest result = projectService.updateProject(projectId, request);

        assertEquals("Updated Title", result.title());
        assertEquals("API", result.type());
        // generalDescription and county should remain unchanged
        assertEquals("A test project", result.generalDescription());
        assertEquals("Monterey", result.county());
    }

    @Test
    void updateProject_throwsWhenNotFound() {
        UpdateProjectRequest request = new UpdateProjectRequest("Title", null, null, null);

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> projectService.updateProject(projectId, request));
    }

    @Test
    void deleteProject_deletesSuccessfully() {
        when(projectRepository.existsById(projectId)).thenReturn(true);

        projectService.deleteProject(projectId);

        verify(projectRepository).deleteById(projectId);
    }

    @Test
    void deleteProject_throwsWhenNotFound() {
        when(projectRepository.existsById(projectId)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> projectService.deleteProject(projectId));
        verify(projectRepository, never()).deleteById(any());
    }
}
