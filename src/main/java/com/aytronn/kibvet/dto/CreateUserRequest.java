package com.aytronn.kibvet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank @Email String username,
        @NotBlank String password) {
}
