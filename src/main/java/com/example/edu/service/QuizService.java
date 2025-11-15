package com.example.edu.service;

import com.example.edu.domain.quiz.AnswerOption;
import com.example.edu.domain.quiz.Question;
import com.example.edu.domain.quiz.Quiz;
import com.example.edu.domain.quiz.QuizSubmission;
import com.example.edu.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

@Service
public class QuizService {

    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final AnswerOptionRepository optionRepo;
    private final QuizSubmissionRepository submissionRepo;
    private final ModuleRepository moduleRepo;
    private final UserRepository userRepo;

    public QuizService(QuizRepository quizRepo,
                       QuestionRepository questionRepo,
                       AnswerOptionRepository optionRepo,
                       QuizSubmissionRepository submissionRepo,
                       ModuleRepository moduleRepo,
                       UserRepository userRepo) {
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
        this.optionRepo = optionRepo;
        this.submissionRepo = submissionRepo;
        this.moduleRepo = moduleRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Quiz createQuiz(Long moduleId, String title) {
        var m = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found: " + moduleId));
        var q = new Quiz();
        q.setCourseModule(m);
        q.setTitle(title);
        return quizRepo.save(q);
    }

    @Transactional
    public Question addQuestion(Long quizId, String text, String type) {
        var q = quizRepo.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + quizId));
        var question = new Question();
        question.setQuiz(q);
        question.setText(text);
        question.setType(parseQuestionType(type)); // строка -> enum (или ваш тип)
        return questionRepo.save(question);
    }

    @Transactional
    public AnswerOption addOption(Long questionId, String text, boolean isCorrect) {
        var question = questionRepo.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + questionId));
        var opt = new AnswerOption();
        opt.setQuestion(question);
        opt.setText(text);
        opt.setCorrect(isCorrect);
        return optionRepo.save(opt);
    }

    @Transactional
    public QuizSubmission submitQuiz(Long quizId, Long studentId, int score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        submissionRepo.findByQuizIdAndStudentId(quizId, studentId)
                .ifPresent(s -> { throw new IllegalStateException("Already taken: quiz=" + quizId + ", student=" + studentId); });

        var quiz    = quizRepo.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + quizId));
        var student = userRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + studentId));

        try {
            var s = new QuizSubmission();
            s.setQuiz(quiz);
            s.setStudent(student);
            s.setScore(score);
            s.setTakenAt(Instant.now());
            return submissionRepo.save(s);
        } catch (DataIntegrityViolationException dup) {
            throw new IllegalStateException("Already taken: quiz=" + quizId + ", student=" + studentId);
        }
    }

    private com.example.edu.domain.enums.QuestionType parseQuestionType(String raw) {
        if (raw == null) throw new IllegalArgumentException("Question type is required");
        String s = raw.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        return switch (s) {
            case "SINGLE", "ONE", "SC", "SINGLE_CHOICE" -> com.example.edu.domain.enums.QuestionType.SINGLE_CHOICE;
            case "MULTI", "MULTIPLE", "MC", "MULTIPLE_CHOICE" -> com.example.edu.domain.enums.QuestionType.MULTIPLE_CHOICE;
            default -> throw new IllegalArgumentException("Unsupported question type: " + raw);
        };
    }
}
