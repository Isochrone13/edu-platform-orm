package com.example.edu.domain.user;

import com.example.edu.domain.BaseEntity;
import com.example.edu.domain.DomainConstraints;
import com.example.edu.domain.course.Assignment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "submissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id","student_id"}))
@Getter
@Setter
public class Submission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    @JsonIgnore
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private User student;

    private Instant submittedAt = Instant.now();

    @Column(length = DomainConstraints.TEXT_LONG)
    private String content;

    private Integer score;
    private String feedback;
}
