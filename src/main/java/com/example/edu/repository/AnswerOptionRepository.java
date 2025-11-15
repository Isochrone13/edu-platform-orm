package com.example.edu.repository;

import com.example.edu.domain.quiz.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestionId(Long questionId);
    long countByQuestionId(Long questionId);
}
