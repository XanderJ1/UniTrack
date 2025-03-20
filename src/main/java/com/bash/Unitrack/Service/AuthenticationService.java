package com.bash.Unitrack.Service;

import com.bash.Unitrack.Controllers.AuthenticationController;
import com.bash.Unitrack.Data.Models.Role;
import com.bash.Unitrack.Data.Models.User;
import com.bash.Unitrack.Repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 TokenService tokenService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    public ResponseEntity<String> register(User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getRole() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enter credentials");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        else {
            User newUser = new User();
            BeanUtils.copyProperties(user, newUser);
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRole().equals(Role.ADMIN)) {
                newUser.setRole(Role.ADMIN);
            } else if (user.getRole().equals(Role.LECTURER)) {
                newUser.setRole(Role.LECTURER);
            } else
                newUser.setRole(Role.STUDENT);

            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        }

    }


    public ResponseEntity<String> signIn(User user) {

        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enter username and password");
        }

        try{
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken
                            (user.getUsername(), user.getPassword()));
            User userr = (User) authentication.getPrincipal();
            return ResponseEntity.ok(tokenService.generate(userr));
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password is incorrect");
        }
    }
}
