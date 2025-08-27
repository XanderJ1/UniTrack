package com.bash.unitrack.service;

import com.bash.unitrack.data.DTO.CourseDTO;
import com.bash.unitrack.data.DTO.CourseRequest;
import com.bash.unitrack.data.models.*;
import com.bash.unitrack.exceptions.NotFoundException;
import com.bash.unitrack.repositories.CourseRepository;
import com.bash.unitrack.repositories.DepartmentRepository;
import com.bash.unitrack.authentication.model.Lecturer;
import com.bash.unitrack.authentication.model.User;
import com.bash.unitrack.authentication.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository, DepartmentRepository departmentRepository){
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public ResponseEntity<List<CourseDTO>> fetchAll() {
        return ResponseEntity.ok(courseRepository.findAll().
                stream().map(CourseDTO::new).collect(Collectors.toList()));
    }

    public ResponseEntity<String> addCourse(CourseRequest courseRequest) throws NotFoundException {

        User user = userRepository.findById(courseRequest.lecturerId())
                .orElseThrow(() -> new NotFoundException("User does not exist"));

        if (!(user instanceof Lecturer)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only lecturers can be assigned courses");
        }
        Lecturer lecturer = (Lecturer) user;
        Department department = departmentRepository.findByDepartmentName(courseRequest.department())
                .orElseThrow(() -> new NotFoundException("Department does not exist"));
        Course newCourse = new Course(courseRequest.courseName(), courseRequest.courseCode(), lecturer, department);
        courseRepository.save(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body("Course added");
    }

    public ResponseEntity<List<Course>> fetchCourse(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));
        if (!(user instanceof Lecturer)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Lecturer lecturer = (Lecturer) user;
        List<Course> courses = lecturer.getCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
}


