package com.bash.Unitrack.Controllers;


import com.bash.Unitrack.Data.DTO.AttendanceDTO;
import com.bash.Unitrack.Data.DTO.SessionDTO;
import com.bash.Unitrack.Data.Models.Attendance;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Service.AttendanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.repository.query.Param;
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
    public ResponseEntity<String > createAttendance(
            @RequestBody AttendanceDTO attendance,
            @RequestParam(required = false) Long id)
            throws NotFoundException, JsonProcessingException {
        return attendanceService.create(attendance, id);
    }


}
