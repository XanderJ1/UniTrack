package com.bash.unitrack.authentication.controller;

import com.bash.unitrack.authentication.dto.UserDTO;
import com.bash.unitrack.authentication.dto.request.Register;
import com.bash.unitrack.exceptions.NotFoundException;
import com.bash.unitrack.authentication.service.AuthenticationService;
import com.bash.unitrack.authentication.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UserService userservice;
    private final AuthenticationService authenticationService;

    public UsersController(UserService userService, AuthenticationService authenticationService){
        this.userservice = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public List<UserDTO> fetchUsers(){
        return userservice.fetchUsers();
    }

    @GetMapping("/profile")
    public UserDTO fetchUser() throws NotFoundException {
        return userservice.fetchUser(authenticationService.getId());
    }

    @PutMapping("/update")
    public ResponseEntity<String > update(@RequestBody Register register) throws NotFoundException {
        return userservice.update(register);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws NotFoundException {
        return userservice.delete(id);
    }
}
