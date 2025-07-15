package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.Models.Attendance;
import com.bash.Unitrack.Data.Models.Lecturer;
import com.bash.Unitrack.Data.Models.Student;

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
