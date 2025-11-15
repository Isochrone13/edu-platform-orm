package com.example.edu.repository;

import com.example.edu.domain.course.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseModuleIdOrderByIdAsc(Long moduleId);
}
