    package com.bash.Unitrack.authentication.service;

    import com.bash.Unitrack.Data.models.*;
    import com.bash.Unitrack.authentication.dto.request.PasswordReset;
    import com.bash.Unitrack.email.EmailService;
    import com.bash.Unitrack.exceptions.BadCredentialsException;
    import com.bash.Unitrack.exceptions.NotFoundException;
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
    import jakarta.mail.MessagingException;
    import jakarta.validation.Valid;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.BeanUtils;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.jwt.Jwt;
    import org.springframework.security.oauth2.jwt.JwtDecoder;
    import org.springframework.stereotype.Service;

    import java.io.IOException;

    @Slf4j
    @Service
    public class AuthenticationService {

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final TokenService tokenService;

        private final AuthenticationManager authenticationManager;
        private final DepartmentRepository departmentRepository;
       private  final EmailService emailService;
       private final JwtDecoder jwtDecoder;

        public AuthenticationService(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager,
                                     TokenService tokenService, DepartmentRepository departmentRepository, EmailService emailService, JwtDecoder jwtDecoder){
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.authenticationManager = authenticationManager;
            this.tokenService = tokenService;
            this.departmentRepository = departmentRepository;
            this.emailService = emailService;
            this.jwtDecoder = jwtDecoder;
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

        public ResponseEntity<String> register(@Valid register register) throws BadCredentialsException, NotFoundException, MessagingException, IOException {
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
                    newUser.setEnabled(false);
                }

                if (register.role().equals("STUDENT")){
                    newUser = new Student();
                    Department department = departmentRepository.findByDepartmentName(register.department())
                            .orElseThrow(() -> new NotFoundException("Course does not exist"));
                    BeanUtils.copyProperties(register, newUser);
                    newUser.setPassword(passwordEncoder.encode(register.password()));
                    newUser.setDepartment(department);
                    newUser.setRole(Role.STUDENT);
                    newUser.setEnabled(false);

                }

                if (register.role().equals("ADMIN")){
                    newUser = new User();
                    BeanUtils.copyProperties(register, newUser);
                    newUser.setPassword(passwordEncoder.encode(register.password()));
                    newUser.setRole(Role.ADMIN);
                }


                userRepository.save(newUser);
                String token = tokenService.generate(CustomUserDetails.build(newUser));
                emailService.verifyEmail(newUser.getFirstName(), newUser.getEmail(), token);
                log.info("EMail sent to{}", newUser.getEmail());
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

    public ResponseEntity<String> requestPasswordReset(PasswordReset body) throws MessagingException, IOException, NotFoundException {
            String email = body.email();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User does not exist"));
            String token = tokenService.generate(CustomUserDetails.build(user));
            emailService.passwordResetEmail(user.getFirstName(), user.getEmail(), token);
            log.info("Password reset link sent to {}", user.getEmail());
            return ResponseEntity.ok("Check your mail ot reset your password");
    }
    public ResponseEntity<String> verifyEmail(String token) throws NotFoundException {
        Jwt jwt = jwtDecoder.decode(token);
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        if (user.isEnabled()){
            return ResponseEntity.ok("User is already verified");
        }

        user.setEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok("Your account has been verified successfully");
    }

        public ResponseEntity<String> passwordReset(String token, String password) throws NotFoundException {
            Jwt jwt = jwtDecoder.decode(token);
            String email = jwt.getSubject();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User does not exist"));
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return ResponseEntity.ok("Your password has been reset successfully");
        }
}
