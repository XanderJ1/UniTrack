package com.bash.Unitrack.Data.DTO;

import com.bash.Unitrack.Data.Models.Location;
import lombok.Data;

@Data
public class SessionDTO {

    private String course;
    private Location location;

    public String getCourse() {
        return course;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCourse(String course) {
        this.course = course;
    }




}
