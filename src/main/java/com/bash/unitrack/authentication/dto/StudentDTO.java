package com.bash.unitrack.authentication.dto;

import com.bash.unitrack.authentication.model.Student;
import lombok.Data;

@Data
public class StudentDTO {


    private String firstName;
    private String lastName;
    private String program;
    private String email;
    private String  username;

    public StudentDTO(Student user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.program = "";
        this.username = user.getUsername();
    }

}
