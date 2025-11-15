package com.example.edu.rest;

import com.example.edu.domain.user.Enrollment;
import com.example.edu.dto.EnrollmentRequestDto;
import com.example.edu.service.EnrollmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@Validated
public class EnrollmentController {
    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Enrollment> enroll(@Valid @RequestBody EnrollmentRequestDto dto) {
        Enrollment e = service.enroll(dto.studentId(), dto.courseId());
        return ResponseEntity.created(URI.create("/api/enrollments/" + e.getId())).body(e);
    }

    @GetMapping("/student/{id}")
    public List<Enrollment> getByStudent(@PathVariable @Positive Long id) {
        return service.getByStudent(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unenroll(@PathVariable @Positive Long id) {
        service.unenroll(id);
        return ResponseEntity.noContent().build();
    }
}
