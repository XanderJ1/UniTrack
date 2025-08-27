    package com.bash.unitrack.controllers;
    
    import com.bash.unitrack.data.DTO.CourseDTO;
    import com.bash.unitrack.data.models.Course;
    import com.bash.unitrack.data.DTO.CourseRequest;
    import com.bash.unitrack.exceptions.NotFoundException;
    import com.bash.unitrack.service.CourseService;
    import com.bash.unitrack.authentication.service.AuthenticationService;
    import jakarta.validation.Valid;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.List;
    
    @RestController
    @RequestMapping("/api/v1/courses")
    public class CourseController {
    
        private final CourseService courseService;
        private final AuthenticationService authenticationService;
    
        public CourseController(CourseService courseService, AuthenticationService authenticationService){
            this.courseService = courseService;
            this.authenticationService = authenticationService;
        }
    
        @GetMapping("")
        public ResponseEntity<List<CourseDTO>> getAll(){
            return courseService.fetchAll();
        }
    
        @GetMapping("/lecturer")
        public ResponseEntity<List<Course>> getLecturerCourses() throws NotFoundException {
            return courseService.fetchCourse(authenticationService.getId());
        }
    
        @PostMapping("/add")
        public ResponseEntity<String > bash(@Valid @RequestBody CourseRequest courseRequest) throws NotFoundException {
            return courseService.addCourse(courseRequest);
        }
    
    }
    
