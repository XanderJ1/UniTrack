package com.bash.Unitrack.controllers;


import com.bash.Unitrack.Data.DTO.AttendanceDT0;
import com.bash.Unitrack.Data.DTO.AttendanceRequestDTO;
import com.bash.Unitrack.Data.models.Attendance;
import com.bash.Unitrack.exceptions.NotFoundException;
import com.bash.Unitrack.Service.AttendanceService;
import com.bash.Unitrack.authentication.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AuthenticationService authenticationService;

    public AttendanceController(AttendanceService attendanceService, AuthenticationService authenticationService){
        this.attendanceService = attendanceService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public ResponseEntity<List<AttendanceDT0>> fetchAll(){
        return attendanceService.fetchAttendance();
    }

    @PostMapping("/mark")
    public ResponseEntity<String > createAttendance(
            @RequestBody AttendanceRequestDTO attendance, @RequestHeader("X-Device_ID") String deviceID,
            @RequestParam(required = false) Long id)
            throws NotFoundException, JsonProcessingException {
        return attendanceService.create(attendance, deviceID, id);
    }

    @GetMapping("/one")
    public ResponseEntity<AttendanceDT0> getAttendance(@RequestParam(required = false) Long id) throws NotFoundException {
        return attendanceService.getAttendance(id);
    }

    @GetMapping("/student")
    public ResponseEntity<List<AttendanceDT0>> studentAttendance() throws NotFoundException {
        return attendanceService.studentAttendance(authenticationService.getId());
    }

    @GetMapping("/lecturer")
    public ResponseEntity<List<Attendance>> attendance() throws NotFoundException {
        return attendanceService.attendanceLecturer(authenticationService.getId());
    }

}