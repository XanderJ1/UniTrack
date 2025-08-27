package com.bash.unitrack.repositories;

import com.bash.unitrack.data.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseName(String courseName);

    boolean existsByCourseCode(String courseCode);
}
