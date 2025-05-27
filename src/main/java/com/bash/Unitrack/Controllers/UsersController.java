package com.bash.Unitrack.Controllers;

import com.bash.Unitrack.Data.DTO.UserDTO;
import com.bash.Unitrack.Data.DTO.UserRequest;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UserService userservice;

    public UsersController(UserService userService){
        this.userservice = userService;
    }

    @GetMapping("")
    public List<UserDTO> fetchUser(){
        return userservice.fetchUsers();
    }

    @PutMapping("/update")
    public ResponseEntity<String > update(@RequestBody UserRequest userRequest) throws NotFoundException {
        return userservice.update(userRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws NotFoundException {
        return userservice.delete(id);
    }
}
