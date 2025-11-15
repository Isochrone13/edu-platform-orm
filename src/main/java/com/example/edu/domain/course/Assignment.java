package com.example.edu.domain.course;

import com.example.edu.domain.BaseEntity;
import com.example.edu.domain.DomainConstraints;
import com.example.edu.domain.user.Submission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignments")
@Getter
@Setter
public class Assignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonIgnore
    private Lesson lesson;

    @Column(nullable = false)
    private String title;

    @Column(length = DomainConstraints.TEXT_LONG)
    private String description;

    private Instant dueDate;
    @Column(name = "max_score")
    private Integer maxScore = 100;

    @OneToMany(mappedBy = "assignment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Submission> submissions = new ArrayList<>();
}
