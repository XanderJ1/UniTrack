package com.bash.unitrack.utilities;

import lombok.Data;

@Data
public class MessageResponse {

    private String message;
    private String status;
    private String error;

    public MessageResponse(String message) {
        this.message = message;
    }
}
