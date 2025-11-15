package com.example.edu.domain.course;

import com.example.edu.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}
