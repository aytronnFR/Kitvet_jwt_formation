package com.aytronn.kibvet.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/editor")
public class EditorController {

    @GetMapping
    public String get() {
        return "GET:: editor controller";
    }

    @PostMapping
    public String post() {
        return "POST:: editor controller";
    }

    @PutMapping
    public String put() {
        return "PUT:: editor controller";
    }

    @DeleteMapping
    public String delete() {
        return "DELETE:: editor controller";
    }
}
