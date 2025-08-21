package com.bash.Unitrack.authentication.controller;

import com.bash.Unitrack.authentication.dto.request.PasswordReset;
import com.bash.Unitrack.authentication.dto.request.SignIn;
import com.bash.Unitrack.authentication.dto.response.signInResponse;
import com.bash.Unitrack.authentication.dto.request.register;
import com.bash.Unitrack.exceptions.BadCredentialsException;
import com.bash.Unitrack.exceptions.NotFoundException;
import com.bash.Unitrack.authentication.service.AuthenticationService;
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
    public ResponseEntity<String> register(@Valid @RequestBody register register) throws BadCredentialsException, NotFoundException, MessagingException, IOException {
        return authenticationService.register(register);
    }

    @PostMapping("/signIn")
    public ResponseEntity<signInResponse> signIn(@Valid @RequestBody SignIn userRequest) throws BadCredentialsException {

        return authenticationService.signIn(userRequest);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String > verifyEmail(@RequestParam(required = true) String token) throws NotFoundException {
        return authenticationService.verifyEmail(token);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String > resetPassword(@RequestParam(required = true) String token, @RequestParam String password) throws NotFoundException {
        return authenticationService.passwordReset(token, password);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordReset email) throws MessagingException, NotFoundException, IOException {
        return authenticationService.requestPasswordReset(email);
    }

}
