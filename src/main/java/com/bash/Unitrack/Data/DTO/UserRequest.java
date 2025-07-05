package com.bash.Unitrack.Data.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank
        String username,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String program,
        String indexNumber,
        @NotBlank
        String password,
        @Email
        String email,
        @NotBlank
        String role
) {}
