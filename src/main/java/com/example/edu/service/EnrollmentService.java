package com.example.edu.service;

import com.example.edu.domain.user.Enrollment;
import com.example.edu.repository.CourseRepository;
import com.example.edu.repository.EnrollmentRepository;
import com.example.edu.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollRepo;
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;

    public EnrollmentService(EnrollmentRepository enrollRepo, UserRepository userRepo, CourseRepository courseRepo) {
        this.enrollRepo = enrollRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
    }

    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        enrollRepo.findByStudentIdAndCourseId(studentId, courseId)
                .ifPresent(e -> { throw new IllegalStateException("Already enrolled: student=" + studentId + ", course=" + courseId); });

        var student = userRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + studentId));
        var course  = courseRepo.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

        try {
            var e = new Enrollment();
            e.setStudent(student);
            e.setCourse(course);
            return enrollRepo.save(e);
        } catch (DataIntegrityViolationException dup) {
            // защита от гонки при уникальном (student, course)
            throw new IllegalStateException("Already enrolled: student=" + studentId + ", course=" + courseId);
        }
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getByStudent(Long studentId) {
        return enrollRepo.findByStudentId(studentId);
    }

    @Transactional
    public void unenroll(Long enrollmentId) {
        enrollRepo.deleteById(enrollmentId);
    }
}
