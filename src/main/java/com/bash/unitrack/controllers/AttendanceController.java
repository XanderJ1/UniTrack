package com.bash.unitrack.controllers;


import com.bash.unitrack.data.DTO.AttendanceDT0;
import com.bash.unitrack.data.DTO.AttendanceRequestDTO;
import com.bash.unitrack.exceptions.NotFoundException;
import com.bash.unitrack.service.AttendanceService;
import com.bash.unitrack.authentication.service.AuthenticationService;
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
            throws NotFoundException {
        return attendanceService.create(attendance, deviceID, id);
    }

    @GetMapping("/one")
    public ResponseEntity<AttendanceDT0> getSpecificAttendance(@RequestParam(required = false) Long id) throws NotFoundException {
        return attendanceService.getSpecificAttendance(id);
    }

   /* @GetMapping("/student")
    public ResponseEntity<List<AttendanceDT0>> studentAttendance() throws NotFoundException {
        return attendanceService.studentAttendance(authenticationService.getId());
    }

    @GetMapping("/lecturer")
    public ResponseEntity<List<Attendance>> attendance() throws NotFoundException {
        return attendanceService.attendanceLecturer(authenticationService.getId());
    }
*/
}