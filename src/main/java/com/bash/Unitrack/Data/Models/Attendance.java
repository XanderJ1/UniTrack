    package com.bash.Unitrack.Data.Models;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.Data;

    import java.time.Instant;
    import java.util.List;

    @Data
    @Entity
    public class Attendance {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @OneToOne
        @JsonBackReference
        private Session session;
        private Instant time;
        @OneToOne
        private Course course;
        @ManyToMany
        private List<Student> student;
        @OneToOne
        private Lecturer lecturer;
        private Status status;

        public Attendance(){

        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Session getSession() {
            return session;
        }

        public void setSession(Session session) {
            this.session = session;
        }

        public Instant getTime() {
            return time;
        }

        public void setTime(Instant time) {
            this.time = time;
        }

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public List<Student> getStudent() {
            return student;
        }

        public void setStudent(List<Student> student) {
            this.student = student;
        }

        public Lecturer getLecturer() {
            return lecturer;
        }

        public void setLecturer(Lecturer lecturer) {
            this.lecturer = lecturer;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Attendance(Session session, Instant time, Lecturer lecturer, Status status){
            this.session = session;
            this.time = time;
            this.lecturer = lecturer;
            this.status = status;
        }


    }
