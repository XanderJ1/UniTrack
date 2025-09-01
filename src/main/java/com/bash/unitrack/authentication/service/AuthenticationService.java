    package com.bash.unitrack.authentication.service;

    import com.bash.unitrack.authentication.dto.request.ResetPassword;
    import com.bash.unitrack.authentication.model.*;
    import com.bash.unitrack.authentication.repository.TokenRepository;
    import com.bash.unitrack.data.models.*;
    import com.bash.unitrack.authentication.dto.request.PasswordReset;
    import com.bash.unitrack.email.EmailService;
    import com.bash.unitrack.exceptions.BadCredentialsException;
    import com.bash.unitrack.exceptions.NotFoundException;
    import com.bash.unitrack.repositories.DepartmentRepository;
    import com.bash.unitrack.authentication.repository.UserRepository;
    import com.bash.unitrack.service.TokenService;
    import com.bash.unitrack.authentication.dto.request.SignIn;
    import com.bash.unitrack.authentication.dto.response.signInResponse;
    import com.bash.unitrack.authentication.dto.request.Register;
    import com.bash.unitrack.authentication.security.CustomUserDetails;
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
    import java.util.Map;

    @Slf4j
    @Service
    public class AuthenticationService {

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;
        private final TokenRepository tokenRepository;

        private final TokenService tokenService;

        private final AuthenticationManager authenticationManager;
        private final DepartmentRepository departmentRepository;
       private  final EmailService emailService;
       private final JwtDecoder jwtDecoder;

        public AuthenticationService(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder, TokenRepository tokenRepository,
                                     AuthenticationManager authenticationManager,
                                     TokenService tokenService, DepartmentRepository departmentRepository, EmailService emailService, JwtDecoder jwtDecoder){
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.tokenRepository = tokenRepository;
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

        public ResponseEntity<String> register(@Valid Register register) throws BadCredentialsException, NotFoundException, MessagingException, IOException {
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
                String token = tokenService.generateVerificationToken(CustomUserDetails.build(newUser));
                emailService.verifyEmail(newUser.getFirstName(), newUser.getEmail(), token);
                log.info("Email sent to {}", newUser.getEmail());
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

    public ResponseEntity<String> verifyEmail(String token) throws NotFoundException {
        Jwt jwt = jwtDecoder.decode(token);
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        if (user.isEnabled()){
            return ResponseEntity.ok("User is already verified");
        }
        if (!(jwt.getClaim("purpose").equals("emailVerification"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Jwt");
        }
        user.setEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok("Your account has been verified successfully");
    }


    public ResponseEntity<String> requestPasswordReset(PasswordReset body) throws MessagingException, IOException, NotFoundException {
        String email = body.email();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        String generatedPasswordToken = tokenService.generatePasswordToken(CustomUserDetails.build(user));
        emailService.passwordResetEmail(user.getFirstName(), user.getEmail(), generatedPasswordToken);
        Token token = Token.builder()
                .token(generatedPasswordToken)
                .isUsed(false)
                .email(email)
                .build();
        tokenRepository.save(token);
        log.info("Password reset link sent to {}", user.getEmail());
        return ResponseEntity.ok("Check your mail to reset your password");
    }

    public ResponseEntity<Map<String, String>> passwordReset(String newToken, ResetPassword password) throws NotFoundException {
        Token token = tokenRepository.findByToken(newToken);
        boolean isUsed = token.isUsed();
        log.info(String.valueOf(isUsed));
        if (isUsed){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Token has been used."));
        }
        User user = userRepository.findByEmail(token.getEmail())
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        token.setUsed(true);
        tokenRepository.save(token);
        log.info(String.valueOf(isUsed));
        user.setPassword(passwordEncoder.encode(password.newPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Your password has been reset successfully"));
    }
}
