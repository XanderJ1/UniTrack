package com.bash.Unitrack.authentication.dto;

import com.bash.Unitrack.authentication.model.Lecturer;

public record LecturerDTO(
        String firstName,
        String lastName,
        String email,
        String role,
        String department
) {
    public LecturerDTO(Lecturer lecturer) {
        this(
                lecturer.getFirstName(),
                lecturer.getLastName(),
                lecturer.getEmail(),
                lecturer.getRole().name(),
                lecturer.getDepartment().getDepartmentName()
        );
    }
}
