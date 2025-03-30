package com.bash.Unitrack.Controllers;

import com.bash.Unitrack.Data.DTO.UserDTO;
import com.bash.Unitrack.Data.Models.User;
import com.bash.Unitrack.Exceptions.BadCredentialsException;
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
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) throws BadCredentialsException {
        return authenticationService.register(userDTO);
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody UserDTO userDTO) throws BadCredentialsException {

        return authenticationService.signIn(userDTO);
    }

}
