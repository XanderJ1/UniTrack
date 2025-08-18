package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.DTO.CourseDTO;
import com.bash.Unitrack.Data.DTO.CourseRequest;
import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.CourseRepository;
import com.bash.Unitrack.Repositories.DepartmentRepository;
import com.bash.Unitrack.authentication.model.Lecturer;
import com.bash.Unitrack.authentication.model.User;
import com.bash.Unitrack.authentication.repository.UserRepository;
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


