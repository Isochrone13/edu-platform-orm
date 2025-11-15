package com.example.edu.domain.quiz;

import com.example.edu.domain.BaseEntity;
import com.example.edu.domain.course.CourseModule;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter
@Setter
public class Quiz extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", unique = true)
    @JsonIgnore
    private CourseModule courseModule;

    private String title;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<QuizSubmission> quizSubmissions = new ArrayList<>();
}
