package com.bash.Unitrack.authentication.dto;

import com.bash.Unitrack.authentication.model.Student;
import lombok.Data;

@Data
public class StudentDTO {


    private String firstName;
    private String lastName;
    private String program;
    private String IndexNumber;
    private String email;

    public StudentDTO(Student user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.program = "";
        this.IndexNumber = "";
    }

}
