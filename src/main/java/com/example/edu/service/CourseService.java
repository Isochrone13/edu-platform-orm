package com.example.edu.service;

import com.example.edu.domain.course.*;
import com.example.edu.domain.user.User;
import com.example.edu.domain.enums.UserRole;
import com.example.edu.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final ModuleRepository moduleRepo;
    private final LessonRepository lessonRepo;

    public CourseService(CourseRepository courseRepo,
                         CategoryRepository categoryRepo,
                         UserRepository userRepo,
                         ModuleRepository moduleRepo,
                         LessonRepository lessonRepo) {
        this.courseRepo = courseRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.moduleRepo = moduleRepo;
        this.lessonRepo = lessonRepo;
    }

    @Transactional
    public Course createCourse(String title, String description, Long categoryId, Long teacherId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + teacherId));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new IllegalStateException("User " + teacherId + " is not a TEACHER");
        }

        Course c = new Course();
        c.setTitle(title);
        c.setDescription(description);
        c.setCategory(category);
        c.setTeacher(teacher);
        return courseRepo.save(c);
    }

    @Transactional
    public CourseModule addModule(Long courseId, String title, int orderIndex) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

        CourseModule module = new CourseModule();
        module.setCourse(course);
        module.setTitle(title);
        module.setOrderIndex(orderIndex);
        return moduleRepo.save(module);
    }

    @Transactional
    public Lesson addLesson(Long moduleId, String title, String content) {
        CourseModule module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found: " + moduleId));

        Lesson lesson = new Lesson();
        lesson.setCourseModule(module);
        lesson.setTitle(title);
        lesson.setContent(content);
        return lessonRepo.save(lesson);
    }

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Course getCourse(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepo.deleteById(id);
    }
}
