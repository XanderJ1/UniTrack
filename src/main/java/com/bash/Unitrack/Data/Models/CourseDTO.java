package com.bash.Unitrack.Data.Models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CourseDTO {

    private String courseName;
    private String courseCode;
    private Long lecturerID;


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Long getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(Long lecturerID) {
        this.lecturerID = lecturerID;
    }
}
