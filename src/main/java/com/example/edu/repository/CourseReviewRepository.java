package com.example.edu.repository;

import com.example.edu.domain.course.CourseReview;
import com.example.edu.domain.course.Course;
import com.example.edu.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findByCourse(Course course);
    List<CourseReview> findByStudent(User student);
}
