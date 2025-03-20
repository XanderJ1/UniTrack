package com.bash.Unitrack.Controllers;

import com.bash.Unitrack.Data.Models.User;
import com.bash.Unitrack.Service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UserService userservice;

    public UsersController(UserService userService){
        this.userservice = userService;
    }

    @GetMapping("")
    public List<String> fetchUsers(){
        return List.of("Akwasi", "Jason");
    }

    @GetMapping("users")
    public List<User> fetchUser(){
        return userservice.fetchUsers();
    }
}
