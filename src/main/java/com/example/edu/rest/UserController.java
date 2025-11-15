package com.example.edu.rest;

import com.example.edu.domain.enums.UserRole;
import com.example.edu.domain.user.User;
import com.example.edu.dto.UserDto;
import com.example.edu.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) { this.repo = repo; }

    @GetMapping
    public List<User> all() { return repo.findAll(); }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserDto req) {
        if (repo.existsByEmail(req.email())) { // есть existsByEmail в репо
            return ResponseEntity.status(409).build();
        }
        User u = new User();
        u.setName(req.name());
        u.setEmail(req.email());
        u.setRole(req.role());
        User saved = repo.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable @Positive Long id) {
        return repo.findById(id).orElseThrow();
    }

    public record CreateUserRequest(
            @NotBlank String name,
            @Email String email,
            @NotBlank UserRole role
    ) {}
}
