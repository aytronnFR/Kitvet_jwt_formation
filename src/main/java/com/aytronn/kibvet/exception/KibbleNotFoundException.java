package com.aytronn.kibvet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class KibbleNotFoundException extends ResourceNotFoundException {

    public KibbleNotFoundException(String message) {
        super(message);
    }
}
