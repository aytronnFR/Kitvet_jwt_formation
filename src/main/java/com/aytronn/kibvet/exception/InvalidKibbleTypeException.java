package com.aytronn.kibvet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidKibbleTypeException extends ResourceNotFoundException {

    private final String kibbleType;

    public InvalidKibbleTypeException(String message, String kibbleType) {
        super(message);
        this.kibbleType = kibbleType;
    }

    @ResponseErrorProperty("kibbleType")
    public String getKibbleType() {
        return this.kibbleType;
    }
}
