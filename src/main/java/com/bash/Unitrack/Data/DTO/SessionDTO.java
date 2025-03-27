package com.bash.Unitrack.Data.DTO;

import lombok.Data;

@Data
public class SessionDTO {

    private String course;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public static class AttendanceDTO {
        private Long sessionId;
        private Long studentId;

        public Long getSessionId() {
            return sessionId;
        }

        public void setSessionId(Long sessionId) {
            this.sessionId = sessionId;
        }

        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }

    }
}
