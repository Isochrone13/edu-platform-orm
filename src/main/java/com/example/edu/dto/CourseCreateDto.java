package com.example.edu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CourseCreateDto(
        @NotBlank @Size(max = DtoConstraints.TEXT_SHORT) String title,
        @NotBlank @Size(max = DtoConstraints.TEXT_LONG)  String description,
        @NotNull @Positive Long categoryId,
        @NotNull @Positive Long teacherId
) {}
