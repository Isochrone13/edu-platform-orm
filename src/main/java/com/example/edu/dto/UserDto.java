// UserDto.java (если это DTO для создания)
package com.example.edu.dto;

import com.example.edu.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotBlank @Size(max = DtoConstraints.TEXT_SHORT) String name,
        @Email   @NotBlank @Size(max = DtoConstraints.TEXT_SHORT) String email,
        @NotNull UserRole role
) {}
