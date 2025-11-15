package com.example.edu.dto;

import com.example.edu.domain.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record QuestionCreateDto(
        @NotNull @Positive Long quizId,
        @NotBlank @Size(max = DtoConstraints.TEXT_LONG) String text,
        @NotNull QuestionType type
) {}
