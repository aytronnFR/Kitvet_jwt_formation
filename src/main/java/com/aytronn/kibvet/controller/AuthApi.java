package com.aytronn.kibvet.controller;

import com.aytronn.kibvet.dto.AuthRequest;
import com.aytronn.kibvet.dto.AuthenticationResponse;
import com.aytronn.kibvet.dto.CreateUserRequest;
import com.aytronn.kibvet.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping(path = "/api/auth")
@Validated
public class AuthApi {

    private final AuthenticationService authenticationService;

    public AuthApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(getAuthenticationService().authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.ok(getAuthenticationService().register(request));
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.ok(getAuthenticationService().test(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getAuthenticationService().refreshToken(request, response);
    }

    public AuthenticationService getAuthenticationService() {
        return this.authenticationService;
    }
}
