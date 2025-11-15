package com.example.edu.repository;

import com.example.edu.domain.user.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    long countByCourseId(Long courseId);
    long countByStudentId(Long studentId);
}
