package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.Models.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
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
