package com.bash.Unitrack.Controllers;

import com.bash.Unitrack.Data.DTO.UserDTO;
import com.bash.Unitrack.Data.Models.User;
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
    public List<User> fetchUser(){
        return userservice.fetchUsers();
    }

    @PutMapping("/update")
    public ResponseEntity<String > update(@RequestBody UserDTO userDTO) throws NotFoundException {
        return userservice.update(userDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws NotFoundException {
        return userservice.delete(id);
    }
}
