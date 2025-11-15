package com.example.edu.domain.user;

import com.example.edu.domain.BaseEntity;
import com.example.edu.domain.enums.EnrollmentStatus;
import com.example.edu.domain.course.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","course_id"}))
@Getter
@Setter
public class Enrollment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @Column(name = "enrolled_at")
    private Instant enrolledAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

}
