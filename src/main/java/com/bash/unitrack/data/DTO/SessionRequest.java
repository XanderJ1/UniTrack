package com.bash.unitrack.data.DTO;

import com.bash.unitrack.data.models.Location;


public record SessionRequest(String courseName, Location location) {
}
