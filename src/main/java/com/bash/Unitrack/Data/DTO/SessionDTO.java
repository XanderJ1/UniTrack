package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.Models.Location;
import com.bash.Unitrack.Data.Models.Session;
import com.bash.Unitrack.Data.Models.Stat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SessionDTO(
        Long id,
        Instant startTime,
        Instant endTime,
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
                session.getStartTime(),
                session.getEndTime(),
                session.getStatus(),
                new CourseRequest(session.getCourse().getCourseName(), session.getCourse().getCourseCode(),session.getLecturer().getId()),
                new LecturerDTO(session.getLecturer()),
                session.getLocation(),
                new AttendanceDT0(session.getAttendance())
        );
    }
}
