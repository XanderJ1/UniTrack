package com.bash.Unitrack.Controllers;

import com.bash.Unitrack.Data.Models.User;
import com.bash.Unitrack.Service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        return authenticationService.register(user);
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody User user){

        return authenticationService.signIn(user);
    }

}
