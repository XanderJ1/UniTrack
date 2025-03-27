package com.bash.Unitrack.Data.Models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CourseDTO {

    private String courseName;
    private String courseCode;
    private String lecturerID;


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

    public String getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }
}
