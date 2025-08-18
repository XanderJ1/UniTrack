package com.bash.Unitrack.authentication.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignIn (
        @NotBlank
        String email,
        @NotBlank
        String password
) {}