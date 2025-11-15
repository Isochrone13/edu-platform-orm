package com.example.edu.repository;

import com.example.edu.domain.quiz.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findByStudentId(Long studentId);
    List<QuizSubmission> findByQuizId(Long quizId);
    Optional<QuizSubmission> findByQuizIdAndStudentId(Long quizId, Long studentId);
}