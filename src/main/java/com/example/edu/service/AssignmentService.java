package com.example.edu.service;

import com.example.edu.domain.course.Assignment;
import com.example.edu.domain.course.Lesson;
import com.example.edu.repository.AssignmentRepository;
import com.example.edu.repository.LessonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AssignmentService {
    private static final Duration DEFAULT_DEADLINE = Duration.ofDays(7);

    private final AssignmentRepository assignmentRepo;
    private final LessonRepository lessonRepo;

    public AssignmentService(AssignmentRepository assignmentRepo, LessonRepository lessonRepo) {
        this.assignmentRepo = assignmentRepo;
        this.lessonRepo = lessonRepo;
    }

    @Transactional
    public Assignment createAssignment(Long lessonId, String title, String description) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + lessonId));

        Assignment a = new Assignment();
        a.setLesson(lesson);
        a.setTitle(title);
        a.setDescription(description);
        a.setDueDate(Instant.now().plus(DEFAULT_DEADLINE));
        return assignmentRepo.save(a);
    }
}
