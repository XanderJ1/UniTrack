package com.bash.Unitrack;

import com.bash.Unitrack.Data.DTO.CourseRequest;
import com.bash.Unitrack.Data.DTO.UserRequest;
import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.CourseRepository;
import com.bash.Unitrack.Repositories.DepartmentRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class Generate {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;

    private final ObjectMapper objectMapper;
    public Generate(UserRepository userRepository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder, CourseRepository courseRepository, ObjectMapper objectMapper){
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
    }

    public void verifyBeforeSave(Department department){
        if (departmentRepository.findByDepartmentName(department.getDepartmentName()).isEmpty()){
            departmentRepository.save(department);
        }
    }
    @Bean
    public CommandLineRunner commandLineRunner() throws NotFoundException {

        Department dept1 = new Department("Art");
        Department dept2 = new Department("Science");
        Department dept3 = new Department("Economics");
        Department dept4 = new Department("Law");
        List<Department> departments = new ArrayList<>(List.of(dept1, dept2,dept3, dept4));

        for(Department department: departments){
            verifyBeforeSave(department);
        }

        User user1 = new User("bash", passwordEncoder.encode("admin"), Role.ADMIN,"bash@gmail");
        Department department = departmentRepository.findByDepartmentName("Science")
                .orElseThrow(() -> new NotFoundException("Department does not exist"));
        System.out.println(user1.getPassword());
        return args -> {
            if (userRepository.findByUsername("bash").isEmpty()){
                user1.setDepartment(department);
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
            List<UserRequest> users = objectMapper.readValue(lecturerFile, new TypeReference<List<UserRequest>>(){});
            for (UserRequest userRequest : users) {
                if (!userRepository.existsByUsername(userRequest.username())) {
                    Department department = departmentRepository.findByDepartmentName("Science").orElseThrow(() -> new NotFoundException("Course does not exist"));
                    Lecturer user = new Lecturer();
                    user.setUsername(userRequest.username());
                    user.setEmail(userRequest.email());
                    user.setFirstName(userRequest.firstName());
                    user.setLastName(userRequest.lastName());
                    user.setDepartment(department);
                    user.setRole(Role.LECTURER);
                    user.setPassword(passwordEncoder.encode(userRequest.password()));
                    userRepository.save(user);
                }
            }

            List<CourseRequest> courses = objectMapper.readValue(courseFile, new TypeReference<List<CourseRequest>>(){});

            for (CourseRequest courseRequest : courses) {
                if (!courseRepository.existsByCourseCode(courseRequest.courseCode())) {
                    Course course = new Course();
                    course.setCourseCode(courseRequest.courseCode());
                    course.setCourseName(courseRequest.courseName());
                    User user = userRepository.findById(courseRequest.lecturerId()).orElseThrow(() -> new NotFoundException("User not found"));
                    if (user instanceof Lecturer lecturer){
                        course.setLecturer(lecturer);
                    }
                    courseRepository.save(course);
                }
            }
            List<UserRequest> students = objectMapper.readValue(studentFile, new TypeReference<List<UserRequest>>(){});
            for (UserRequest userRequest : students) {
                if (!userRepository.existsByUsername(userRequest.username())) {
                    Department department = departmentRepository.findByDepartmentName("Science")
                            .orElseThrow(() -> new NotFoundException("Course does not exist"));
                    Student student = new Student();
                    student.setUsername(userRequest.username());
                    student.setEmail(userRequest.email());
                    student.setFirstName(userRequest.firstName());
                    student.setLastName(userRequest.lastName());
                    student.setDepartment(department);
                    student.setIndexNumber(userRequest.indexNumber());
                    student.setRole(Role.STUDENT);
                    student.setPassword(passwordEncoder.encode(userRequest.password()));
                    userRepository.save(student);
                }
            }
        };
    }
}


