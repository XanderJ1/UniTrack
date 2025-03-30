package com.bash.Unitrack.Data.DTO;

import lombok.Data;

@Data
public class SessionDTO {

    private String course;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }


}
