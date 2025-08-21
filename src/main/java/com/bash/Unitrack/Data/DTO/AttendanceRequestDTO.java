package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.models.Location;

public record AttendanceRequestDTO(Long sessionId, Location location) {}
