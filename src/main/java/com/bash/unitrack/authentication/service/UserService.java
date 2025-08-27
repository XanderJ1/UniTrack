package com.bash.unitrack.authentication.service;

import com.bash.unitrack.exceptions.NotFoundException;
import com.bash.unitrack.authentication.model.Lecturer;
import com.bash.unitrack.authentication.model.Student;
import com.bash.unitrack.authentication.model.User;
import com.bash.unitrack.authentication.repository.UserRepository;
import com.bash.unitrack.authentication.dto.UserDTO;
import com.bash.unitrack.authentication.dto.request.register;
import com.bash.unitrack.authentication.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    String errorMessage = "User does not exist";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
        if(!user.isEnabled()){
            throw new UsernameNotFoundException("User account is not activated. Please verify your email first");
        }
        return CustomUserDetails.build(user);
    }

    public List<UserDTO> fetchUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public ResponseEntity<String> update(register register) throws NotFoundException {
        User user = userRepository.findByUsername(register.username())
                .orElseThrow(() -> new NotFoundException(errorMessage));

        if (register.firstName() != null){
            user.setFirstName(register.firstName());
        }
        if (register.lastName() != null){
            user.setLastName(register.lastName());
        }
        if (register.email() != null){
            user.setEmail(register.email());
        }
        if (register.password() != null){
            user.setPassword(passwordEncoder.encode(register.password()));
        }

        if (user instanceof Student student){
            if (register.program() != null){
                student.setProgram(register.program());
            }
            if (register.indexNumber() != null){
                student.setIndexNumber(register.indexNumber());
            }
            userRepository.save(student);
            return ResponseEntity.ok("User details updated");
        }

        userRepository.save(user);
        return ResponseEntity.ok("User details updated");
    }

    public ResponseEntity<String> delete(Long id) throws NotFoundException {

        User user = userRepository.findById(id).
                orElseThrow(() -> new NotFoundException(errorMessage));
        userRepository.delete(user);
        return ResponseEntity.ok("User deleted");
    }

    public UserDTO fetchUser(Long id) throws NotFoundException {
        User user = userRepository.findById(id).
                orElseThrow(() -> new NotFoundException(errorMessage));
        if (user instanceof Lecturer){
            Lecturer lecturer = (Lecturer) user;
        }
        else{
            Student student = (Student) user;
        }
        return new UserDTO(user);

    }
}

