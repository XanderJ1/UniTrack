package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.DTO.SessionDTO;
import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Repositories.AttendanceRepository;
import com.bash.Unitrack.Repositories.CourseRepository;
import com.bash.Unitrack.Repositories.SessionRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final CourseRepository courseRepository;

    public SessionService(
            SessionRepository sessionRepository,
            UserRepository userRepository,
            AttendanceRepository attendanceRepository,
            CourseRepository courseRepository){
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.courseRepository = courseRepository;
    }


    public ResponseEntity<List<Session>> fetchSession() {
        return ResponseEntity.ok(sessionRepository.findAll());
    }

    public ResponseEntity<String> createSession(SessionDTO sessionDTO) {

        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = (Jwt) object;
        String  username = jwt.getSubject();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        }
        if (user.get().getRole().equals(Role.STUDENT)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You should be a lecturer to create a session");
        }
        Optional<Course> courseOptional = courseRepository.findByCourseName(sessionDTO.getCourse());
        if (courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course does not exist");
        }
        Course course = courseOptional.get();
        Lecturer lecturer = (Lecturer) user.get();
        Session session = new Session();
        Attendance attendance = new Attendance();
        userRepository.save(lecturer);
        courseRepository.save(course);
        sessionRepository.save(session);

        //Set up attendance
        attendance.setLecturer(lecturer);
        attendance.setCourse(course);
        attendance.setTime(Instant.now());
        attendance.setSession(session);
        attendanceRepository.save(attendance);

        //Setup Session
        session.setAttendance(attendance);
        session.setCourse(course);
        session.setLecturer(lecturer);
        session.setStartTime(Instant.now());
        session.setStatus(Stat.ACTIVE);
        session.setEndTime(Instant.now().plusSeconds(3600));

        sessionRepository.save(session);


        return ResponseEntity.ok("Session Started");
    }
}





