package com.bash.Unitrack.Data.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private String courseId;
    @OneToOne
    private Attendance attendance;
    @OneToOne
    private Session session;
    @OneToMany
    private List<Student> student;
    @OneToOne
    private Lecturer lecturer;

    public Course(){

    }

    public Course(String courseName, String courseId, Lecturer lecturer){
        this.courseName = courseName;
        this.courseId = courseId;
        this.lecturer = lecturer;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<Student> getStudent() {
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}
