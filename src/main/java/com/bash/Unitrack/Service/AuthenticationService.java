package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.DTO.SignIn;
import com.bash.Unitrack.Data.DTO.SignInRequest;
import com.bash.Unitrack.Data.DTO.UserRequest;
import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Exceptions.BadCredentialsException;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.DepartmentRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;
    private final DepartmentRepository departmentRepository;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 TokenService tokenService, DepartmentRepository departmentRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.departmentRepository = departmentRepository;
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

    public ResponseEntity<String> register(@Valid UserRequest userRequest) throws BadCredentialsException, NotFoundException {
        if (userRequest.username() == null || userRequest.password() == null || userRequest.role() == null) {
            throw new BadCredentialsException("Enter credentials");
        }
        if (userRepository.findByUsername(userRequest.username()).isPresent()) {
            throw new BadCredentialsException("User already exists");
        }
        else {
            User newUser = new User();
            if (userRequest.role().equals("LECTURER")){
                newUser = new Lecturer();
                Department department = departmentRepository.findByDepartmentName(userRequest.department())
                        .orElseThrow(() -> new NotFoundException("Course does not exist"));
                BeanUtils.copyProperties(userRequest, newUser);
                newUser.setDepartment(department);
                newUser.setPassword(passwordEncoder.encode(userRequest.password()));
                newUser.setRole(Role.LECTURER);
            }

            if (userRequest.role().equals("STUDENT")){
                newUser = new Student();
                Department department = departmentRepository.findByDepartmentName(userRequest.department())
                        .orElseThrow(() -> new NotFoundException("Course does not exist"));
                BeanUtils.copyProperties(userRequest, newUser);
                newUser.setPassword(passwordEncoder.encode(userRequest.password()));
                newUser.setDepartment(department);
                newUser.setRole(Role.STUDENT);
            }

            if (userRequest.role().equals("ADMIN")){
                newUser = new User();
                BeanUtils.copyProperties(userRequest, newUser);
                newUser.setPassword(passwordEncoder.encode(userRequest.password()));
                newUser.setRole(Role.ADMIN);
            }

            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        }

    }

    public ResponseEntity<SignInRequest> signIn(@Valid SignIn userRequest) throws BadCredentialsException {

        if (userRequest.username() == null || userRequest.password() == null) {
            throw new BadCredentialsException("Enter username and password");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (userRequest.username(), userRequest.password()));
        User user = (User) authentication.getPrincipal();
        String role = user.getRole().toString();
        String jwt = tokenService.generate(user);
        return ResponseEntity.ok(new SignInRequest(jwt, role));
    }
}
