package com.bash.Unitrack;

import com.bash.Unitrack.Data.DTO.CourseDTO;
import com.bash.Unitrack.Data.DTO.UserDTO;
import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.CourseRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Configuration
public class Generate {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;

    private final ObjectMapper objectMapper;
    public Generate(UserRepository userRepository, PasswordEncoder passwordEncoder, CourseRepository courseRepository, ObjectMapper objectMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
    }
    @Bean
    public CommandLineRunner commandLineRunner(){

        User user1 = new User("bash", passwordEncoder.encode("admin"), Role.ADMIN,"bash@gmail");
        System.out.println(user1.getPassword());
        return args -> {
            if (userRepository.findByUsername("bash").isEmpty()){
                userRepository.save(user1);
            }
        };
    }

    @Profile("local")
    @Bean
    public CommandLineRunner userDetailLoader() throws NotFoundException {

        return args -> {
            File lecturerFile = new File("C:\\Users\\user\\Downloads\\lecturerData.json");
            File courseFile = new File("C:\\Users\\user\\Downloads\\courseData.json");
            File studentFile = new File("C:\\Users\\user\\Downloads\\studentData.json");

            if (!studentFile.exists() || !courseFile.exists() || !lecturerFile.exists()) {
                throw new NotFoundException("File not found: " + lecturerFile.getAbsolutePath());
            }
            List<UserDTO> users = objectMapper.readValue(lecturerFile, new TypeReference<List<UserDTO>>(){});
            for (UserDTO userDTO : users) {
                if (!userRepository.existsByUsername(userDTO.getUsername())) {
                    Lecturer user = new Lecturer();
                    user.setUsername(userDTO.getUsername());
                    user.setEmail(userDTO.getEmail());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    user.setRole(Role.LECTURER);
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    userRepository.save(user);
                }
            }

            List<CourseDTO> courses = objectMapper.readValue(courseFile, new TypeReference<List<CourseDTO>>(){});

            for (CourseDTO courseDTO: courses) {
                if (!courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
                    Course course = new Course();
                    course.setCourseCode(courseDTO.getCourseCode());
                    course.setCourseName(courseDTO.getCourseName());
                    User user = userRepository.findById(courseDTO.getLecturerId()).orElseThrow(() -> new NotFoundException("User not found"));
                    Lecturer lecturer = new Lecturer();
                    if (user instanceof Lecturer){
                        lecturer = (Lecturer) user;
                        course.setLecturer(lecturer);
                    }
                    courseRepository.save(course);
                }
            }
            List<UserDTO> students = objectMapper.readValue(studentFile, new TypeReference<List<UserDTO>>(){});
            for (UserDTO userDTO : students) {
                if (!userRepository.existsByUsername(userDTO.getUsername())) {
                    Student student = new Student();
                    student.setUsername(userDTO.getUsername());
                    student.setEmail(userDTO.getEmail());
                    student.setFirstName(userDTO.getFirstName());
                    student.setLastName(userDTO.getLastName());
                    student.setIndexNumber(userDTO.getIndexNumber());
                    student.setRole(Role.STUDENT);
                    student.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    userRepository.save(student);
                }
            }
        };
    }
}


