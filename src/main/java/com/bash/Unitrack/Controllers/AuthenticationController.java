package com.bash.Unitrack.Controllers;

import com.bash.Unitrack.Data.DTO.SignIn;
import com.bash.Unitrack.Data.DTO.SignInRequest;
import com.bash.Unitrack.Data.DTO.UserRequest;
import com.bash.Unitrack.Exceptions.BadCredentialsException;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Service.AuthenticationService;
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
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest) throws BadCredentialsException, NotFoundException {
        return authenticationService.register(userRequest);
    }

    @PostMapping("/signIn")
    public ResponseEntity<SignInRequest> signIn(@Valid @RequestBody SignIn userRequest) throws BadCredentialsException {

        return authenticationService.signIn(userRequest);
    }

}
