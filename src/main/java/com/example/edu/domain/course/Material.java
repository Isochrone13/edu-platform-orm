package com.example.edu.domain.course;

import com.example.edu.domain.BaseEntity;
import com.example.edu.domain.enums.ResourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "materials")
@Getter
@Setter
public class Material extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonIgnore
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Column(nullable = false)
    private String title;

    private String url;
    private String description;
}
