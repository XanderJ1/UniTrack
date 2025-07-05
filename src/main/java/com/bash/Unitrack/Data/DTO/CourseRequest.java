package com.bash.Unitrack.Data.DTO;

import jakarta.validation.Valid;

public record CourseRequest(@Valid String courseName, String courseCode, Long lecturerId) {}
