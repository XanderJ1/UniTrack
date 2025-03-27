package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.DTO.SessionDTO;
import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Repositories.AttendanceRepository;
import com.bash.Unitrack.Repositories.SessionRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    public final AttendanceRepository attendanceRepository;
    public final UserRepository userRepository;
    public final SessionRepository sessionRepository;

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

    public ResponseEntity<String> create(SessionDTO.AttendanceDTO attendanceDTO) {

        Optional<Session> sessionOptional = sessionRepository.findById(attendanceDTO.getSessionId());
        if (sessionOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session does not exist");
        }
        Optional<User> studentOptional = userRepository.findById(attendanceDTO.getStudentId());
        if (studentOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }

//        Student student = (Student) studentOptional.get();
        Session session = sessionOptional.get();
        Attendance attendance = session.getAttendance();
//        userRepository.save(student);
//        List<Student> students = new ArrayList<>();
//        students.add(student);
//        attendance.setStudent(students);
//        student.getAttendance().add(attendance);
        attendanceRepository.save(attendance);
//        userRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body("Attendance Marked");
    }

}
