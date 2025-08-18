package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.Models.Location;
import com.bash.Unitrack.Data.Models.Session;
import com.bash.Unitrack.Data.Models.Stat;
import com.bash.Unitrack.authentication.dto.LecturerDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.*;

public record SessionDTO(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Stat status,
        CourseRequest course,
        @JsonProperty("lecturer")
        LecturerDTO lecturerDTO,
        Location location,
        AttendanceDT0 attendance
) {
    public SessionDTO(Session session) {
        this(
                session.getId(),
                session.getStartTime().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                session.getEndTime().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                session.getStatus(),
                new CourseRequest(session.getCourse().getCourseName(), session.getCourse().getCourseCode(), session.getLecturer().getId(), session.getLecturer().getDepartment().getDepartmentName()),
                new LecturerDTO(session.getLecturer()),
                session.getLocation(),
                new AttendanceDT0(session.getAttendance())
        );
    }
}
