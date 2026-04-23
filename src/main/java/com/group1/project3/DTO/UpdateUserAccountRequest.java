package com.group1.project3.DTO;

import jakarta.validation.constraints.Email;

public record UpdateUserAccountRequest(
        String username,
        String password,
        @Email String email) {
}
