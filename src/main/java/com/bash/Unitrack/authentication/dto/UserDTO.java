package com.bash.Unitrack.authentication.dto;

import com.bash.Unitrack.authentication.model.User;

public record UserDTO (

    String firstName,
    String lastName,
    String department,
    String program,
    String email
){
    public UserDTO(User user) {
        this(
        user.getFirstName(),
        user.getLastName(),
        user.getDepartment().getDepartmentName(),
        "",
        user.getEmail()
        );
    }
}
