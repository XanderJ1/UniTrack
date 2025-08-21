package com.bash.Unitrack.Data.models;

import com.bash.Unitrack.authentication.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String departmentName;
    @ManyToMany
    List<Course> courses;
    @OneToMany
    List<User> users;

    public Department(){

    }

    public Department(String departmentName){
        this.departmentName = departmentName;
    }
}
