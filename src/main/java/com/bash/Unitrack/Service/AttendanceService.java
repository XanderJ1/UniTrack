package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.DTO.AttendanceDTO;
import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.AttendanceRepository;
import com.bash.Unitrack.Repositories.SessionRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import com.bash.Unitrack.Utilities.RequestClass;
import com.bash.Unitrack.Utilities.RouteRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    public final AttendanceRepository attendanceRepository;
    public final UserRepository userRepository;
    public final SessionRepository sessionRepository;
    public final AuthenticationService authenticationService;
    private final WebClient webclient;

    Session sessions = new Session();

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            SessionRepository sessionRepository,
            WebClient webclient,
            AuthenticationService authenticationService){
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.authenticationService = authenticationService;
        this.webclient = webclient;
    }

    public ResponseEntity<List<Attendance>> fetchAttendance() {
        return ResponseEntity.ok(attendanceRepository.findAll());
    }

    public ResponseEntity<String> commandLineRunner(
            Location lecturerLocation,
            Location studentLocation) throws JsonProcessingException {

        String apiKey = "AIzaSyBdMRVS5_VcpweMHk27ZP_GkXFWMHmEgco";

        RequestClass request = new RequestClass(
                lecturerLocation.getLatitude(), lecturerLocation.getLongitude()-4,
                studentLocation.getLatitude(), studentLocation.getLongitude()-3);

        request.setTravelMode("DRIVE");
        RouteRequest routeRequest = new RouteRequest(webclient);

        String value = routeRequest.computeRoute(request, apiKey)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(request));
        return ResponseEntity.ok(value);
    }

    public ResponseEntity<String> create(AttendanceDTO attendanceDTO, @RequestParam(required = false) Long id) throws NotFoundException, JsonProcessingException {

        Session session = sessionRepository.findById(attendanceDTO.getSessionId())
                .orElseThrow(() -> new NotFoundException("Session does not exist"));

        if (Instant.now().isAfter(session.getEndTime())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session is closed");
        }

        Student student = new Student();

        String username = authenticationService.getUsername();
        User currentUser = userRepository.findByUsername(username).orElseThrow();
        if (currentUser instanceof Lecturer){
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Student does not exist"));

            if (!(user instanceof Student)){
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
        attendance.setLocation(attendanceDTO.getLocation());
        student.getAttendance().add(attendance);
        attendanceRepository.save(attendance);
        userRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body("Attendance Marked" +
                commandLineRunner(session.getLocation(), attendance.getLocation()).getBody());
    }
}