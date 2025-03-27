package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.Models.*;
import com.bash.Unitrack.Repositories.CourseRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public ResponseEntity<String> addCourse(CourseDTO courseDTO) {

        Optional<User> lecturerOptional = userRepository.findById(2L);

        if (lecturerOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }

        if (!(lecturerOptional.get() instanceof Lecturer)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only lecturers can be assigned courses");
        }
        Lecturer lecturer = (Lecturer) lecturerOptional.get();
        Course newCourse = new Course(courseDTO.getCourseName(), courseDTO.getCourseCode(), lecturer);
        courseRepository.save(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created");
    }
}


