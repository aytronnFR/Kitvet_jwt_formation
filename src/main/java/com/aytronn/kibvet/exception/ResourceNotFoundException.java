package com.aytronn.kibvet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3771014075422572866L;

    /**
     * @param message the message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}