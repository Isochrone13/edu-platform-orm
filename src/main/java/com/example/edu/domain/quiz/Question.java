package com.example.edu.domain.quiz;

import com.example.edu.domain.DomainConstraints;
import com.example.edu.domain.BaseEntity;
import com.example.edu.domain.enums.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonIgnore
    private Quiz quiz;

    @Column(nullable=false, length=DomainConstraints.TEXT_MEDIUM)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private QuestionType type;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AnswerOption> options = new ArrayList<>();
}
