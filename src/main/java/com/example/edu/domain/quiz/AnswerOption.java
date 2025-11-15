package com.example.edu.domain.quiz;

import com.example.edu.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "answer_options")
@Getter
@Setter
public class AnswerOption extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private Question question;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false, name = "is_correct")
    private boolean isCorrect;
}
