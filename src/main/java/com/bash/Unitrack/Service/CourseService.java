package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Exceptions.NotFoundException;
import com.bash.Unitrack.Repositories.CourseRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository){
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<Course>> fetchAll() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    public ResponseEntity<String> addCourse(CourseDTO courseDTO) throws NotFoundException {

        User user = userRepository.findById(courseDTO.getLecturerID())
                .orElseThrow(() -> new NotFoundException("User does not exist"));

        if (!(user instanceof Lecturer)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only lecturers can be assigned courses");
        }
        Lecturer lecturer = (Lecturer) user;
        Course newCourse = new Course(courseDTO.getCourseName(), courseDTO.getCourseCode(), lecturer);
        courseRepository.save(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created");
    }
}


