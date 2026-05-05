package com.group1.project3.controller;

import com.group1.project3.DTO.CreateRoleRequest;
import com.group1.project3.DTO.GetRoleResponseRequest;
import com.group1.project3.DTO.UpdateRoleRequest;
import com.group1.project3.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<GetRoleResponseRequest>> getRolesByProjectId(@PathVariable UUID projectId) {
        return ResponseEntity.ok(roleService.getRolesByProjectId(projectId));
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<GetRoleResponseRequest> getRoleById(@PathVariable UUID roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    @PostMapping
    public ResponseEntity<GetRoleResponseRequest> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(request));
    }

    @PatchMapping("/{roleId}")
    public ResponseEntity<GetRoleResponseRequest> updateRole(@PathVariable UUID roleId, @Valid @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(roleService.updateRole(roleId, request));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
