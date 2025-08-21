package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.models.Attendance;
import com.bash.Unitrack.authentication.dto.StudentDTO;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

public record AttendanceDT0 (
     Long id,
     String courseName,
     List<StudentDTO> studentList,
     String lecturer,
     LocalDateTime date
){
    public AttendanceDT0(Attendance attendance) {
        this(
        attendance.getId(),
        attendance.getCourse().getCourseName(),
        attendance.getStudents().stream().map(StudentDTO::new).collect(Collectors.toList()),
attendance.getLecturer().getFirstName() + " " + attendance.getLecturer().getLastName(),
        attendance.getTime().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }

}
