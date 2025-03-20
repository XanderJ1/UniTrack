package com.bash.Unitrack;

import com.bash.Unitrack.Data.Models.Role;
import com.bash.Unitrack.Data.Models.User;
import com.bash.Unitrack.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Generate {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Generate(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner commandLineRunner(){

        User user1 = new User("bash", passwordEncoder.encode("admin"), Role.ADMIN,"bash@gmail");
        System.out.println(user1.getPassword());
        return args -> {
            userRepository.save(user1);
        };
    }
}
