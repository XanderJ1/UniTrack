package com.bash.Unitrack.Controllers;


import com.bash.Unitrack.Data.DTO.AttendanceDTO;
import com.bash.Unitrack.Data.DTO.SessionDTO;
import com.bash.Unitrack.Data.Models.Attendance;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService){
        this.attendanceService = attendanceService;
    }

    @GetMapping("")
    public ResponseEntity<List<Attendance>> fetchAll(){
        return attendanceService.fetchAttendance();
    }

    @PostMapping("/mark")
    public ResponseEntity<String > createAttendance(@RequestBody AttendanceDTO attendance) throws NotFoundException {
        return attendanceService.create(attendance);
    }


}
