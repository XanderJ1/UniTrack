package com.bash.Unitrack.Data.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseDTO {

    private String courseName;
    private String courseCode;
    private Long lecturerId;

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Long getLecturerId() {
        return lecturerId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
    }
}
