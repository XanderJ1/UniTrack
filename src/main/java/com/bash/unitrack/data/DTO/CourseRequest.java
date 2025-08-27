package com.bash.unitrack.data.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record CourseRequest(@Valid String courseName, String courseCode, Long lecturerId, @NotBlank String department) {}
