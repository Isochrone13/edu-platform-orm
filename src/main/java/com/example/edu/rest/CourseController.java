package com.example.edu.rest;

import com.example.edu.domain.course.Course;
import com.example.edu.dto.CourseCreateDto;
import com.example.edu.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Validated
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Course> create(@Valid @RequestBody CourseCreateDto dto) {
        Course c = service.createCourse(dto.title(), dto.description(), dto.categoryId(), dto.teacherId());
        return ResponseEntity.created(URI.create("/api/courses/" + c.getId())).body(c);
    }

    @GetMapping
    public List<Course> all() { return service.getAllCourses(); }

    @GetMapping("/{id}")
    public Course get(@PathVariable @Positive Long id) { return service.getCourse(id); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        service.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
