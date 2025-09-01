package com.bash.unitrack.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

public record Register(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
                String username,

        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name cannot exceed 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        String lastName,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
                message = "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
        )
        String password,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Role is required")
        @Pattern(regexp = "STUDENT|LECTURER|ADMIN", message = "Role must be STUDENT, LECTURER, or ADMIN")
        String role,

        @NotBlank(message = "Department is required")
        @Size(max = 100, message = "Department cannot exceed 100 characters")
        String department,

        @Size(max = 100, message = "Program cannot exceed 100 characters")
        String program,

        @Pattern(regexp = "\\d{4,10}", message = "Index number must be 4-10 digits")
        String indexNumber
) {}
