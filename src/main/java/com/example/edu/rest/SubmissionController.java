package com.example.edu.rest;

import com.example.edu.domain.user.Submission;
import com.example.edu.repository.SubmissionRepository;
import com.example.edu.service.SubmissionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@Validated
public class SubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionRepository submissionRepo;

    public SubmissionController(SubmissionService submissionService,
                                SubmissionRepository submissionRepo) {
        this.submissionService = submissionService;
        this.submissionRepo = submissionRepo;
    }

    @PostMapping
    public ResponseEntity<Submission> submit(@RequestParam @Positive Long assignmentId,
                                             @RequestParam @Positive Long studentId,
                                             @RequestParam String content) {
        Submission s = submissionService.submit(assignmentId, studentId, content);
        return ResponseEntity.created(URI.create("/api/submissions/" + s.getId())).body(s);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getById(@PathVariable @Positive Long id) {
        return submissionRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<Submission>> getByAssignment(@PathVariable @Positive Long assignmentId) {
        return ResponseEntity.ok(submissionRepo.findByAssignmentId(assignmentId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Submission>> getByStudent(@PathVariable @Positive Long studentId) {
        return ResponseEntity.ok(submissionRepo.findByStudentId(studentId));
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<Submission> grade(@PathVariable @Positive Long id,
                                            @RequestParam int score,
                                            @RequestParam(required = false) String feedback) {
        Submission updated = submissionService.grade(id, score, feedback);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        if (submissionRepo.existsById(id)) {
            submissionRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
