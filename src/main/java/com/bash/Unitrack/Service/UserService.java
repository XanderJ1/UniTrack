package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.DTO.UserDTO;
import com.bash.Unitrack.Data.Models.User;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }

    public List<User> fetchUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<String> update(UserDTO userDTO) throws NotFoundException {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new NotFoundException(message));

        if (userDTO.getFirstName() != null){
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null){
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null){
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
}

