package com.bash.unitrack.controllers;

import com.bash.unitrack.data.DTO.SessionDTO;
import com.bash.unitrack.data.DTO.SessionRequest;
import com.bash.unitrack.exceptions.NotFoundException;
import com.bash.unitrack.service.SessionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @GetMapping("")
    public ResponseEntity<Page<SessionDTO>> fetchAllService(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) throws NotFoundException {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return sessionService.fetchAllSession(pageable);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SessionDTO>> fetchActiveService(){
        return sessionService.fetchActiveSession();
    }

    @GetMapping("/closed")
    public ResponseEntity<List<SessionDTO>> fetchClosedService(){
        return sessionService.fetchClosedSession();
    }

    @PostMapping("/create")
    public ResponseEntity<String > create(
            @RequestBody SessionRequest sessionRequest,
            @RequestParam Integer time) throws NotFoundException {
        return sessionService.createSession(sessionRequest, time);
    }
}
