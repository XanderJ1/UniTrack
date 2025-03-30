    package com.bash.Unitrack.Data.Models;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;

    import java.time.Instant;
    import java.util.ArrayList;
    import java.util.List;

    @Data
    @Entity
    @AllArgsConstructor
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
        @ManyToMany(mappedBy = "attendance")
        private List<Student> students = new ArrayList<>();
        @OneToOne
        private Lecturer lecturer;
        public Attendance(){

        }

        public Attendance(Session session, Instant time, Lecturer lecturer, Status status){
            this.session = session;
            this.time = time;
            this.lecturer = lecturer;
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
            return students;
        }

        public void setStudent(List<Student> student) {
            this.students = student;
        }

        public Lecturer getLecturer() {
            return lecturer;
        }

        public void setLecturer(Lecturer lecturer) {
            this.lecturer = lecturer;
        }



    }
