package com.example.edu.rest;

import com.example.edu.domain.quiz.*;
import com.example.edu.dto.AnswerOptionCreateDto;
import com.example.edu.dto.QuestionCreateDto;
import com.example.edu.service.QuizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/quizzes")
@Validated
public class QuizController {
    private final QuizService service;

    public QuizController(QuizService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestParam @Positive Long moduleId,
                                           @RequestParam String title) {
        Quiz q = service.createQuiz(moduleId, title);
        return ResponseEntity.created(URI.create("/api/quizzes/" + q.getId())).body(q);
    }

    @PostMapping("/question")
    public ResponseEntity<Question> addQuestion(@Valid @RequestBody QuestionCreateDto dto) {
        var saved = service.addQuestion(dto.quizId(), dto.text(), dto.type().name());
        return ResponseEntity.created(URI.create("/api/quizzes/question/" + saved.getId()))
                .body(saved);
    }

    @PostMapping("/option")
    public ResponseEntity<AnswerOption> addOption(@Valid @RequestBody AnswerOptionCreateDto dto) {
        var saved = service.addOption(dto.questionId(), dto.text(), dto.isCorrect());
        return ResponseEntity.created(URI.create("/api/quizzes/option/" + saved.getId()))
                .body(saved);
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<QuizSubmission> submit(@PathVariable @Positive Long quizId,
                                                 @RequestParam @Positive Long studentId,
                                                 @RequestParam int score) {
        QuizSubmission s = service.submitQuiz(quizId, studentId, score);
        return ResponseEntity.created(URI.create("/api/submissions/" + s.getId())).body(s);
    }
}
