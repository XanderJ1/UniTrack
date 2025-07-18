package com.bash.Unitrack.Data.Models;

import ch.qos.logback.core.sift.Discriminator;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
