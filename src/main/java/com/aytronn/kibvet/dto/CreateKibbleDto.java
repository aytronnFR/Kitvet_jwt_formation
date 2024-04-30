package com.aytronn.kibvet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateKibbleDto(
        @NotBlank String name,
        String description,
        @NotBlank String range,
        @NotBlank String type,
        @NotBlank String mark,
        @NotNull @Valid FoodCreateDto food
) {
}
