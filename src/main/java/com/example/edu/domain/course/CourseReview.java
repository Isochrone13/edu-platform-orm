package com.example.edu.domain.course;

import com.example.edu.domain.BaseEntity;
import com.example.edu.domain.DomainConstraints;
import com.example.edu.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "course_reviews")
@Getter
@Setter
public class CourseReview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private User student;

    @Column(nullable = false)
    private Integer rating; // 1..5

    @Column(length= DomainConstraints.TEXT_LONG)
    private String comment;

    @Transient
    @JsonProperty(value = "createdAtReview", access = JsonProperty.Access.READ_ONLY)
    public java.time.Instant getCreatedAtReview() {
        return this.createdAt;
    }

}
