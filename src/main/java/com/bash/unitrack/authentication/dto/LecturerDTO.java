package com.bash.unitrack.authentication.dto;

import com.bash.unitrack.authentication.model.Lecturer;

public record LecturerDTO(
        String firstName,
        String lastName,
        String email,
        String role,
        String department,
        String username
) {
    public LecturerDTO(Lecturer lecturer) {
        this(
                lecturer.getFirstName(),
                lecturer.getLastName(),
                lecturer.getEmail(),
                lecturer.getRole().name(),
                lecturer.getDepartment().getDepartmentName(),
                lecturer.getUsername()
        );
    }
}
