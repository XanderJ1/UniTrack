package com.bash.unitrack.service;

import com.bash.unitrack.data.DTO.AttendanceDT0;
import com.bash.unitrack.data.DTO.AttendanceRequestDTO;
import com.bash.unitrack.data.models.*;
import com.bash.unitrack.exceptions.DeviceUsedException;
import com.bash.unitrack.exceptions.NotFoundException;
import com.bash.unitrack.repositories.AttendanceRepository;
import com.bash.unitrack.repositories.DeviceIDRepository;
import com.bash.unitrack.repositories.SessionRepository;
import com.bash.unitrack.authentication.model.Lecturer;
import com.bash.unitrack.authentication.model.Student;
import com.bash.unitrack.authentication.model.User;
import com.bash.unitrack.authentication.repository.UserRepository;
import com.bash.unitrack.utilities.Haversine;
import com.bash.unitrack.utilities.RequestClass;
import com.bash.unitrack.utilities.RouteRequest;
import com.bash.unitrack.authentication.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    public final AttendanceRepository attendanceRepository;
    public final UserRepository userRepository;
    public final SessionRepository sessionRepository;
    public final AuthenticationService authenticationService;
    private final DeviceIDRepository deviceIDRepository;
    private final WebClient webclient;



    Session sessions = new Session();

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            SessionRepository sessionRepository,
            WebClient webclient,
            AuthenticationService authenticationService, DeviceIDRepository deviceIDRepository){
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.authenticationService = authenticationService;
        this.webclient = webclient;
        this.deviceIDRepository = deviceIDRepository;
    }

    public ResponseEntity<List<AttendanceDT0>> fetchAttendance() {
        return ResponseEntity.ok(attendanceRepository.findAll()
                .stream().map(AttendanceDT0::new).collect(Collectors.toList()));
    }

    public ResponseEntity<String> range(
            Location lecturerLocation,
            Location studentLocation) throws JsonProcessingException {

        String apiKey = "AIzaSyBdMRVS5_VcpweMHk27ZP_GkXFWMHmEgco";

        RequestClass request = new RequestClass(
                lecturerLocation.getLatitude(), lecturerLocation.getLongitude(),
                studentLocation.getLatitude(), studentLocation.getLongitude());

        request.setTravelMode("WALK");

        ObjectMapper mapper = new ObjectMapper();
        System.out.println("\n The request x" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request) + "\n");

        RouteRequest routeRequest = new RouteRequest(webclient);

        String value = routeRequest.computeRoute(request, apiKey)
                .block();

        if (value == null) {
            return ResponseEntity.ok("Value is null");
        }
        return ResponseEntity.ok(value);
    }

    public double range2(RequestClass request){

        return Haversine.calculateDistance(request);
    }

    public void isDeviceUsed(User currentUser, String deviceID){
        Optional<Device> deviceOptional = deviceIDRepository.findDeviceByDeviceID(deviceID);
        if (deviceOptional.isEmpty()){
            System.out.println(deviceID);
            deviceIDRepository.save(new Device(Instant.now(), deviceID));
        }else {
            // Check If device has already taken attendance 1 minute ago
            Device device = deviceOptional.get();
            if (currentUser instanceof Student && device.getDeviceID().equals(deviceID)
                    && device.getTime().isAfter(Instant.now()
                    .minusSeconds(60))){
                System.out.println("User has already marked attendance");
                throw new DeviceUsedException("Device has already been used \n" + "Wait 1 minute to try again");
            }
        }
    }

    public ResponseEntity<String> create(
            AttendanceRequestDTO attendanceRequestDTO,
            @RequestHeader("X-Device-ID") String deviceID,
            @RequestParam(required = false) Long id) throws NotFoundException {

        Session session = sessionRepository.findById(attendanceRequestDTO.sessionId())
                .orElseThrow(() -> new NotFoundException("Session does not exist"));

        if (session.getStatus() == Stat.CLOSED){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session is closed");
        }

        RequestClass requestClass = new RequestClass(
                session.getLocation().getLatitude(),
                session.getLocation().getLongitude(),
                attendanceRequestDTO.location().getLatitude(),
                attendanceRequestDTO.location().getLongitude()
        );

        Student student = new Student();

        String email = authenticationService.getEmail();
        User currentUser = userRepository.findByEmail(email).orElseThrow();
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

        //Check if device has been used
        isDeviceUsed(currentUser, deviceID);

        double distance = range2(requestClass);

        System.out.println("Haversine " + distance);
        if (distance >= 0.025){
            double excess = Math.round((distance - 0.025) * 1000);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body("You're out of range: " + excess + " metres from location");
        }

        Attendance attendance1 = session.getAttendance();
        List<Student> presentStudents = attendance1.getStudents();
        for(Student student1 : presentStudents){
            if (student1.getId() == currentUser.getId()){
                throw new DeviceUsedException("User has already taken attendance");
            }
        }

        Attendance attendance = session.getAttendance();
        sessions = session;
        userRepository.save(student);
        List<Student> students = new ArrayList<>();
        students.add(student);
        attendance.setStudent(students);
        attendance.setLocation(attendanceRequestDTO.location());
        student.getAttendance().add(attendance);
        attendanceRepository.save(attendance);
        userRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body("Attendance Marked");
    }

    public ResponseEntity<AttendanceDT0> getSpecificAttendance(Long id) throws NotFoundException {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attendance does not exist"));
        AttendanceDT0 attendanceDT0 = new AttendanceDT0(attendance);
        return ResponseEntity.ok(attendanceDT0);
    }

    /*public ResponseEntity<List<AttendanceDT0>> studentAttendance(Long id) throws NotFoundException {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        Student student = (Student) user;
        List<Attendance> attendanceList =  student.getAttendance();
        return ResponseEntity.status(HttpStatus.OK).body(attendanceList.stream().map(AttendanceDT0::new).collect(Collectors.toList()));
    }

    public ResponseEntity<List<Attendance>> attendanceLecturer(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        Lecturer lecturer = (Lecturer) user;
        List<Attendance> attendanceList = lecturer.getAttendance();
        return ResponseEntity.ok(attendanceList);
    }
*/
}