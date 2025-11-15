package com.example.edu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EnrollmentRequestDto(
        @NotNull @Positive Long studentId,
        @NotNull @Positive Long courseId
) {}