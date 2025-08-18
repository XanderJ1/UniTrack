package com.bash.Unitrack.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record register(
        @NotBlank
        String username,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String password,
        @Email
        String email,
        @NotBlank
        String role,
        @NotBlank
        String department,
        String program,
        String indexNumber
) {}
