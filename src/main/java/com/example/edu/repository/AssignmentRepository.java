package com.example.edu.repository;

import com.example.edu.domain.course.Assignment;
import com.example.edu.domain.course.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByLessonId(Long lessonId);
}