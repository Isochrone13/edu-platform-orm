package com.example.edu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AnswerOptionCreateDto(
        @NotNull @Positive Long questionId,
        @NotBlank @Size(max = DtoConstraints.TEXT_SHORT) String text,
        @NotNull @JsonProperty("correct") Boolean isCorrect
) {}
