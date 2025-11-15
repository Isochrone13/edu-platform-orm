package com.example.edu.repository;

import com.example.edu.domain.quiz.Question;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByQuizId(Long quizId);

    // подтянуть опции для одного вопроса (без N+1)
    @Query("select q from Question q left join fetch q.options where q.id = :id")
    Optional<Question> fetchWithOptions(@Param("id") Long id);

    // разом подтянуть опции для всех вопросов викторины
    @Query("select distinct q from Question q left join fetch q.options where q.quiz.id = :quizId")
    List<Question> fetchAllWithOptionsByQuizId(@Param("quizId") Long quizId);
}
