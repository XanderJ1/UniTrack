package com.bash.unitrack.data.DTO;
import com.bash.unitrack.data.models.Course;
import com.bash.unitrack.authentication.dto.LecturerDTO;

import java.util.List;
import java.util.stream.Collectors;

public record CourseDTO(String courseName, String courseCode, List<LecturerDTO> lecturerId) {
    public CourseDTO(Course course) {
        this(
                course.getCourseName(),
                course.getCourseCode(),
                course.getLecturer().stream().map(LecturerDTO::new)
                        .collect(Collectors.toList())
        );
    }
}
