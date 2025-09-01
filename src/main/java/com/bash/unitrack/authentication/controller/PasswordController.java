package com.bash.unitrack.authentication.controller;

import com.bash.unitrack.authentication.dto.request.PasswordReset;
import com.bash.unitrack.authentication.dto.request.ResetPassword;
import com.bash.unitrack.authentication.service.AuthenticationService;
import com.bash.unitrack.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/web")
public class PasswordController {

    private final AuthenticationService authenticationService;

    public PasswordController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/password-reset")
    public String passwordPage(@RequestParam(required = false) String token){
        return "PasswordPage";
    }

    @PostMapping("/password-reset")
    public @ResponseBody ResponseEntity<Map<String, String>> passwordReset(
            @RequestParam(required = true) String token,
            @RequestBody ResetPassword password) throws NotFoundException {
        return authenticationService.passwordReset(token, password);
    }
}
