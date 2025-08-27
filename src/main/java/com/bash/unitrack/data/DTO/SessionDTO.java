package com.bash.unitrack.data.DTO;

import com.bash.unitrack.data.models.Location;
import com.bash.unitrack.data.models.Session;
import com.bash.unitrack.data.models.Stat;
import com.bash.unitrack.authentication.dto.LecturerDTO;
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
