package com.bash.Unitrack.authentication.service;

import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.authentication.model.Lecturer;
import com.bash.Unitrack.authentication.model.Student;
import com.bash.Unitrack.authentication.model.User;
import com.bash.Unitrack.authentication.repository.UserRepository;
import com.bash.Unitrack.authentication.dto.UserDTO;
import com.bash.Unitrack.authentication.dto.request.register;
import com.bash.Unitrack.authentication.security.CustomUserDetails;
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

    String message = "User does not exist";

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

        return CustomUserDetails.build(user);
    }

    public List<UserDTO> fetchUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public ResponseEntity<String> update(register register) throws NotFoundException {
        User user = userRepository.findByUsername(register.username())
                .orElseThrow(() -> new NotFoundException(message));

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
                orElseThrow(() -> new NotFoundException(message));
        userRepository.delete(user);
        return ResponseEntity.ok("User deleted");
    }

    public UserDTO fetchUser(Long id) throws NotFoundException {
        User user = userRepository.findById(id).
                orElseThrow(() -> new NotFoundException(message));
        if (user instanceof Lecturer){
            Lecturer lecturer = (Lecturer) user;
        }
        else{
            Student lecturer = (Student) user;
        }
        return new UserDTO(user);

    }
}

