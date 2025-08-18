package com.bash.Unitrack.authentication.model;

import com.bash.Unitrack.Data.Models.Attendance;
import com.bash.Unitrack.Data.Models.Course;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@DiscriminatorValue("student")
public class Student extends User {

    private String indexNumber;
    private String program;
    @ManyToMany
    @JoinTable(
            name = "student_attendance",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "attendance_id")
    )
    @JsonBackReference
    private List<Attendance> attendance = new ArrayList<>();
    @ManyToMany
    private List<Course> courses;


}
