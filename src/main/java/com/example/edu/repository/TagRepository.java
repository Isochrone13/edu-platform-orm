package com.example.edu.repository;

import com.example.edu.domain.course.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
}
