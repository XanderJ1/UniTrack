package com.bash.Unitrack.Data.Models;

import ch.qos.logback.core.sift.Discriminator;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("lecturer")
public class Lecturer extends User {

    private String lecturerId;
    private String name;
    private String department;

    @OneToMany
    private List<Session> sessions;
    @OneToMany
    private List<Attendance> attendance;

    public Lecturer(){

    }

    public Lecturer(String name, String department){
        this.name = name;
        this.department = department;
    }

}
