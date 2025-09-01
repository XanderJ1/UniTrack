package com.bash.unitrack.authentication.controller;

import com.bash.unitrack.authentication.dto.request.PasswordReset;
import com.bash.unitrack.authentication.dto.request.SignIn;
import com.bash.unitrack.authentication.dto.response.signInResponse;
import com.bash.unitrack.authentication.dto.request.Register;
import com.bash.unitrack.exceptions.BadCredentialsException;
import com.bash.unitrack.exceptions.NotFoundException;
import com.bash.unitrack.authentication.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody Register register) throws BadCredentialsException, NotFoundException, MessagingException, IOException {
        return authenticationService.register(register);
    }

    @PostMapping("/signIn")
    public ResponseEntity<signInResponse> signIn(@Valid @RequestBody SignIn userRequest) throws BadCredentialsException {

        return authenticationService.signIn(userRequest);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String > verifyEmail(@RequestParam(required = true) String token) throws NotFoundException {
        return authenticationService.verifyEmail(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordReset email) throws MessagingException, NotFoundException, IOException {
        return authenticationService.requestPasswordReset(email);
    }

}
