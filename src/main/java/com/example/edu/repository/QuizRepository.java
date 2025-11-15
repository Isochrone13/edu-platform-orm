package com.example.edu.repository;

import com.example.edu.domain.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByCourseModuleId(Long moduleId);
}
