package com.bash.Unitrack.authentication.controller;

import com.bash.Unitrack.authentication.dto.request.SignIn;
import com.bash.Unitrack.authentication.dto.response.signInResponse;
import com.bash.Unitrack.authentication.dto.request.register;
import com.bash.Unitrack.Exceptions.BadCredentialsException;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> register(@Valid @RequestBody register register) throws BadCredentialsException, NotFoundException {
        return authenticationService.register(register);
    }

    @PostMapping("/signIn")
    public ResponseEntity<signInResponse> signIn(@Valid @RequestBody SignIn userRequest) throws BadCredentialsException {

        return authenticationService.signIn(userRequest);
    }

}
