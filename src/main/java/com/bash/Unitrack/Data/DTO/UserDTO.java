package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.Models.User;

public record UserDTO (

    String username,
    String firstName,
    String lastName,
    String department,
    String program,
    String email
){
    public UserDTO(User user) {
        this(
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getDepartment().getDepartmentName(),
        user.getEmail(),
        ""
        );
    }
}
