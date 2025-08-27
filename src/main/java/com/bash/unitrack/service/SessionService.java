package com.bash.unitrack.service;

import com.bash.unitrack.data.DTO.SessionDTO;
import com.bash.unitrack.data.DTO.SessionRequest;
import com.bash.unitrack.data.models.*;
import com.bash.unitrack.exceptions.NotFoundException;
import com.bash.unitrack.repositories.AttendanceRepository;
import com.bash.unitrack.repositories.CourseRepository;
import com.bash.unitrack.repositories.SessionRepository;
import com.bash.unitrack.authentication.model.Lecturer;
import com.bash.unitrack.authentication.model.Role;
import com.bash.unitrack.authentication.model.User;
import com.bash.unitrack.authentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public ResponseEntity<Page<SessionDTO>> fetchAllSession(Pageable pageable) throws NotFoundException {

        Page<Session> sessions = sessionRepository.findAll(pageable);
        if (sessions.isEmpty()) {
            log.warn("No sessions found");
            throw new NotFoundException("No sessions found");
        }
        Page<SessionDTO> sessionDTOs = new PageImpl<>(
                sessions.stream()
                        .map(SessionDTO::new)
                        .collect(Collectors.toList()),
                pageable,
                sessions.getTotalElements());
        return ResponseEntity.ok(sessionDTOs);
    }

    @Cacheable(value = "sessions:closed")
    public ResponseEntity<List<SessionDTO>> fetchClosedSession() {
        return ResponseEntity.ok(sessionRepository.findByStatus(Stat.CLOSED)
                .stream()
                .map(SessionDTO::new)
                .collect(Collectors.toList()));
    }

    public ResponseEntity<List<SessionDTO>> fetchActiveSession() {
        return ResponseEntity.ok(sessionRepository.findByStatus(Stat.ACTIVE)
                .stream().map(SessionDTO::new)
                .collect(Collectors.toList()));
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    @CacheEvict(value = {"sessions:active", "sessions:closed"}, allEntries = true)
    public void closeExpiredSession(){
        List<Session> expiredSession = sessionRepository.findByStatusAndEndTimeBefore(Stat.ACTIVE, Instant.now());
        if (expiredSession.isEmpty()){
            return;
        }
        expiredSession.forEach(session -> {
            session.setStatus(Stat.CLOSED);
        });
        sessionRepository.saveAll(expiredSession);
    }

    public Boolean isInRange(Location studentLocation, Location lecturerLocation){
        return null;
    }


    @CacheEvict(value = {"sessions:active", "sessions:closed"}, allEntries = true)
    public ResponseEntity<String> createSession(SessionRequest sessionRequest, Integer dateTime) throws NotFoundException {

        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = (Jwt) object;
        String  email = jwt.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        if (user.getRole().equals(Role.STUDENT)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You should be a lecturer to create a session");
        }
        Optional<Course> courseOptional = courseRepository.findByCourseName(sessionRequest.courseName());
        if (courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course does not exist");
        }
        Course course = courseOptional.get();
        Lecturer lecturer = (Lecturer) user;
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
        session.setLocation(sessionRequest.location());
        session.setCourse(course);
        session.setLecturer(lecturer);
        session.setStartTime(Instant.now());
        session.setStatus(Stat.ACTIVE);
        session.setEndTime(Instant.now().plusSeconds(dateTime));
        log.info("Session ends in {}", dateTime);

        sessionRepository.save(session);
        return ResponseEntity.ok("Session Started");
    }
}





