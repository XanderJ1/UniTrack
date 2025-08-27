package com.bash.unitrack.authentication.dto;

import com.bash.unitrack.authentication.model.User;

public record UserDTO (

    String firstName,
    String lastName,
    String department,
    String program,
    String email,
    String username
){
    public UserDTO(User user) {
        this(
        user.getFirstName(),
        user.getLastName(),
        user.getDepartment().getDepartmentName(),
        "",
        user.getEmail(),
        user.getUsername()
        );
    }
}
