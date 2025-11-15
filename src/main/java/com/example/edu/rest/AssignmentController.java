package com.example.edu.rest;

import com.example.edu.domain.course.Assignment;
import com.example.edu.domain.user.Submission;
import com.example.edu.service.AssignmentService;
import com.example.edu.service.SubmissionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/assignments")
@Validated
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;

    public AssignmentController(AssignmentService assignmentService, SubmissionService submissionService) {
        this.assignmentService = assignmentService;
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<Assignment> create(@RequestParam @Positive Long lessonId,
                                             @RequestParam String title,
                                             @RequestParam String description) {
        Assignment created = assignmentService.createAssignment(lessonId, title, description);
        return ResponseEntity.created(URI.create("/api/assignments/" + created.getId()))
                .body(created);
    }

    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<Submission> submit(@PathVariable @Positive Long assignmentId,
                                             @RequestParam @Positive Long studentId,
                                             @RequestParam String content) {
        Submission s = submissionService.submit(assignmentId, studentId, content);
        return ResponseEntity.created(URI.create("/api/submissions/" + s.getId()))
                .body(s);
    }

    @PutMapping("/grade/{submissionId}")
    public ResponseEntity<Submission> grade(@PathVariable @Positive Long submissionId,
                                            @RequestParam int score,
                                            @RequestParam(required = false) String feedback) {
        return ResponseEntity.ok(submissionService.grade(submissionId, score, feedback));
    }
}
