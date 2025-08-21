package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.models.Location;


public record SessionRequest(String courseName, Location location) {
}
