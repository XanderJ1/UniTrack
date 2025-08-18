package com.bash.Unitrack.authentication.service;

import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Exceptions.BadCredentialsException;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.DepartmentRepository;
import com.bash.Unitrack.authentication.model.Lecturer;
import com.bash.Unitrack.authentication.model.Role;
import com.bash.Unitrack.authentication.model.Student;
import com.bash.Unitrack.authentication.model.User;
import com.bash.Unitrack.authentication.repository.UserRepository;
import com.bash.Unitrack.Service.TokenService;
import com.bash.Unitrack.authentication.dto.request.SignIn;
import com.bash.Unitrack.authentication.dto.response.signInResponse;
import com.bash.Unitrack.authentication.dto.request.register;
import com.bash.Unitrack.authentication.security.CustomUserDetails;
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

    public String getEmail(){
        Object context = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = (Jwt) context;
        return jwt.getSubject();
    }

    public Long getId(){
        Object context = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = (Jwt) context;
        return jwt.getClaim("user_id");
    }

    public ResponseEntity<String> register(@Valid register register) throws BadCredentialsException, NotFoundException {
        if (register.username() == null || register.password() == null || register.role() == null) {
            throw new BadCredentialsException("Enter credentials");
        }
        if (userRepository.findByEmail(register.email()).isPresent()) {
            throw new BadCredentialsException("User already exists");
        }
        else {
            User newUser = new User();
            if (register.role().equals("LECTURER")){
                newUser = new Lecturer();
                Department department = departmentRepository.findByDepartmentName(register.department())
                        .orElseThrow(() -> new NotFoundException("Course does not exist"));
                BeanUtils.copyProperties(register, newUser);
                newUser.setDepartment(department);
                newUser.setPassword(passwordEncoder.encode(register.password()));
                newUser.setRole(Role.LECTURER);
            }

            if (register.role().equals("STUDENT")){
                newUser = new Student();
                Department department = departmentRepository.findByDepartmentName(register.department())
                        .orElseThrow(() -> new NotFoundException("Course does not exist"));
                BeanUtils.copyProperties(register, newUser);
                newUser.setPassword(passwordEncoder.encode(register.password()));
                newUser.setDepartment(department);
                newUser.setRole(Role.STUDENT);
            }

            if (register.role().equals("ADMIN")){
                newUser = new User();
                BeanUtils.copyProperties(register, newUser);
                newUser.setPassword(passwordEncoder.encode(register.password()));
                newUser.setRole(Role.ADMIN);
            }

            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        }

    }

    public ResponseEntity<signInResponse> signIn(@Valid SignIn userRequest) throws BadCredentialsException {

        if (userRequest.email() == null || userRequest.password() == null) {
            throw new BadCredentialsException("Enter username and password");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (userRequest.email(), userRequest.password()));
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String role = user.getAuthorities().stream().toList().getFirst().toString();
        String jwt = tokenService.generate(user);
        return ResponseEntity.ok(new signInResponse(jwt, role));
    }
}
