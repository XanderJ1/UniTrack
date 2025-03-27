package com.bash.Unitrack.Controllers;

import com.bash.Unitrack.Data.Models.Course;
import com.bash.Unitrack.Data.Models.CourseDTO;
import com.bash.Unitrack.Data.Models.Lecturer;
import com.bash.Unitrack.Data.Models.Role;
import com.bash.Unitrack.Repositories.CourseRepository;
import com.bash.Unitrack.Repositories.UserRepository;
import com.bash.Unitrack.Service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @GetMapping("")
    public ResponseEntity<List<Course>> getAll(){
        return courseService.fetchAll();
    }

    @PostMapping("/add")
    public ResponseEntity<String > bash(@RequestBody CourseDTO courseDTO){
        return courseService.addCourse(courseDTO);
    }

}
