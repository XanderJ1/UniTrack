package com.bash.Unitrack.authentication.model;

import com.bash.Unitrack.Data.models.Attendance;
import com.bash.Unitrack.Data.models.Course;
import com.bash.Unitrack.Data.models.Session;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("lecturer")
public class Lecturer extends User {

    private String lecturerId;
    private String name;
    @ManyToMany
    private List<Course> courses;
    @OneToMany
    @JsonBackReference
    private List<Session> sessions;
    @OneToMany(mappedBy = "lecturer")
    @JsonBackReference
    private List<Attendance> attendance;
    public Lecturer(){

    }

    public Lecturer(String name){
        this.name = name;
    }

}
