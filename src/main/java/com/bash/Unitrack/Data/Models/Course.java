package com.bash.Unitrack.Data.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private String courseCode;
    @OneToOne
    private Attendance attendance;
    @OneToOne
    private Session session;
    @OneToMany
    private List<Student> student;
    @ManyToMany
    private List<Lecturer> lecturer = new ArrayList<>();

    public Course(){

    }

    public Course(String courseName, String courseId, Lecturer lecturer){
        this.courseName = courseName;
        this.courseCode = courseId;
        this.lecturer.add(lecturer);
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseId) {
        this.courseCode = courseId;
    }

    public List<Student> getStudent() {
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }

    public List<Lecturer> getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer.add(lecturer);
    }
}
