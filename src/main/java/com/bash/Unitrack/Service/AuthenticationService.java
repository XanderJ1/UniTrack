package com.bash.Unitrack.Service;

import com.bash.Unitrack.Controllers.AuthenticationController;
import com.bash.Unitrack.Data.DTO.UserDTO;
import com.bash.Unitrack.Data.Models.Lecturer;
import com.bash.Unitrack.Data.Models.Role;
import com.bash.Unitrack.Data.Models.Student;
import com.bash.Unitrack.Data.Models.User;
import com.bash.Unitrack.Exceptions.BadCredentialsException;
import com.bash.Unitrack.Repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
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

    public String getUsername(){
        Object context = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = (Jwt) context;
        return jwt.getSubject();
    }

    public Long getId(){
        Object context = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = (Jwt) context;
        return jwt.getClaim("user_id");
    }

    public ResponseEntity<String> register(UserDTO userDTO) throws BadCredentialsException {
        if (userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getRole() == null) {
            throw new BadCredentialsException("Enter credentials");
        }
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new BadCredentialsException("User already exists");
        }
        else {

            User newUser = new User();
            if (userDTO.getRole().equals("LECTURER")){
                newUser = new Lecturer();
                BeanUtils.copyProperties(userDTO, newUser);
                newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                newUser.setRole(Role.LECTURER);
            }

            if (userDTO.getRole().equals("STUDENT")){
                newUser = new Student();
                BeanUtils.copyProperties(userDTO, newUser);
                newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                newUser.setRole(Role.STUDENT);
            }

            if (userDTO.getRole().equals("ADMIN")){
                newUser = new User();
                BeanUtils.copyProperties(userDTO, newUser);
                newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                newUser.setRole(Role.ADMIN);
            }

            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        }

    }

    public ResponseEntity<String> signIn(UserDTO userDTO) throws BadCredentialsException {

        if (userDTO.getUsername() == null || userDTO.getPassword() == null) {
            throw new BadCredentialsException("Enter username and password");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (userDTO.getUsername(), userDTO.getPassword()));
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(tokenService.generate(user));
    }
}
