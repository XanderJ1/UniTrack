package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.Models.Location;

public class AttendanceRequestDTO {
    private Long sessionId;
    private Location location;

    public Long getSessionId() {
        return sessionId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

}
