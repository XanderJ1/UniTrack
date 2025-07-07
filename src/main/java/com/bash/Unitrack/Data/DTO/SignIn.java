package com.bash.Unitrack.Data.DTO;

import jakarta.validation.constraints.NotBlank;

public record SignIn (
        @NotBlank
        String username,
        @NotBlank
        String password
) {}