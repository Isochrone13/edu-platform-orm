package com.example.edu.service;

import com.example.edu.domain.user.Submission;
import com.example.edu.repository.AssignmentRepository;
import com.example.edu.repository.SubmissionRepository;
import com.example.edu.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepo;
    private final AssignmentRepository assignmentRepo;
    private final UserRepository userRepo;

    public SubmissionService(SubmissionRepository submissionRepo,
                             AssignmentRepository assignmentRepo,
                             UserRepository userRepo) {
        this.submissionRepo = submissionRepo;
        this.assignmentRepo = assignmentRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Submission submit(Long assignmentId, Long studentId, String content) {
        submissionRepo.findByAssignmentIdAndStudentId(assignmentId, studentId)
                .ifPresent(s -> { throw new IllegalStateException("Already submitted: assignment=" + assignmentId + ", student=" + studentId); });

        var a = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found: " + assignmentId));
        var student = userRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + studentId));

        var s = new Submission();
        s.setAssignment(a);
        s.setStudent(student);
        s.setContent(content);
        s.setSubmittedAt(Instant.now());
        return submissionRepo.save(s);
    }

    @Transactional
    public Submission grade(Long submissionId, int score, String feedback) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        var s = submissionRepo.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found: " + submissionId));
        s.setScore(score);
        s.setFeedback(feedback);
        return submissionRepo.save(s);
    }
}
