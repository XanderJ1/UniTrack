package com.bash.unitrack.controllers;

import com.bash.unitrack.data.models.Location;
import com.bash.unitrack.authentication.model.User;
import com.bash.unitrack.authentication.repository.UserRepository;
import com.bash.unitrack.authentication.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final AuthenticationService authService;
    private final UserRepository userRepository;

    public LocationController(AuthenticationService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public ResponseEntity<String> location(@RequestBody Location location){
        Optional<User> user = userRepository.findById(authService.getId());
        String email = "";
        if (user.isPresent()){
            email = user.get().getEmail();
        }
        System.out.println(location.getLatitude());
        System.out.println(location.getLongitude());
        return ResponseEntity.ok("Location Captured" + email);
    }
}
