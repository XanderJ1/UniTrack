package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.DTO.AttendanceDTO;
import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.AttendanceRepository;
import com.bash.Unitrack.Repositories.SessionRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    public final AttendanceRepository attendanceRepository;
    public final UserRepository userRepository;
    public final SessionRepository sessionRepository;

    Session sessions = new Session();

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            SessionRepository sessionRepository){
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public ResponseEntity<List<Attendance>> fetchAttendance() {
        return ResponseEntity.ok(attendanceRepository.findAll());
    }

    public ResponseEntity<String> create(AttendanceDTO attendanceDTO) throws NotFoundException {

        Session session = sessionRepository.findById(attendanceDTO.getSessionId())
                .orElseThrow(() -> new NotFoundException("Session does not exist"));

        if (Instant.now().isAfter(session.getEndTime())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session is closed");
        }

        Student student = new Student();

        Object context = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = (Jwt) context;
        String username = jwt.getSubject();
        User currentUser = userRepository.findByUsername(username).get();
        if (currentUser instanceof Lecturer){
            User user = userRepository.findById(attendanceDTO.getStudentId())
                    .orElseThrow(() -> new NotFoundException("Student does not exist"));

            if (!(student instanceof Student)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not a student");
            }
            student = (Student) user;
        }

        if (currentUser instanceof Student){
            student = (Student) currentUser;
        }

        Attendance attendance = session.getAttendance();

        sessions = session;
        userRepository.save(student);
        List<Student> students = new ArrayList<>();
        students.add(student);
        attendance.setStudent(students);
        student.getAttendance().add(attendance);
        attendanceRepository.save(attendance);
        userRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body("Attendance Marked");
    }
}