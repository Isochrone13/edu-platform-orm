package com.example.edu.tests;

import com.example.edu.domain.course.Category;
import com.example.edu.domain.course.Course;
import com.example.edu.domain.course.CourseModule;
import com.example.edu.domain.course.Lesson;
import com.example.edu.domain.enums.EnrollmentStatus;
import com.example.edu.domain.course.Assignment;
import com.example.edu.domain.user.Submission;
import com.example.edu.domain.user.Enrollment;
import com.example.edu.domain.user.User;
import com.example.edu.domain.enums.UserRole;
import com.example.edu.repository.AssignmentRepository;
import com.example.edu.repository.CategoryRepository;
import com.example.edu.repository.CourseRepository;
import com.example.edu.repository.EnrollmentRepository;
import com.example.edu.repository.LessonRepository;
import com.example.edu.repository.ModuleRepository;
import com.example.edu.repository.SubmissionRepository;
import com.example.edu.repository.UserRepository;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CrudTests {

    @Autowired CategoryRepository categoryRepo;
    @Autowired UserRepository userRepo;
    @Autowired CourseRepository courseRepo;
    @Autowired ModuleRepository moduleRepo;
    @Autowired LessonRepository lessonRepo;
    @Autowired AssignmentRepository assignmentRepo;
    @Autowired SubmissionRepository submissionRepo;
    @Autowired EnrollmentRepository enrollmentRepo;

    // -------------------- CREATE + CASCADE --------------------

    @Test
    void createCourseWithModulesAndLessons_cascadePersist() {
        var category = categoryRepo.saveAndFlush(newCategory("Programming"));
        var teacher  = userRepo.saveAndFlush(newUser("Teacher One", uniqueEmail("t1"), UserRole.TEACHER));

        // root-aggregate: Course -> Modules (cascade) -> Lessons (cascade)
        var course = new Course();
        course.setTitle("Java Core " + uniq());
        course.setDescription("Intro + Collections + OOP");
        course.setCategory(category);
        course.setTeacher(teacher);

        var m1 = new CourseModule();
        m1.setCourse(course);
        m1.setTitle("Basics");
        m1.setOrderIndex(1);

        var l11 = new Lesson();
        l11.setCourseModule(m1);
        l11.setTitle("Variables");
        l11.setContent("Types, var, primitives");

        var l12 = new Lesson();
        l12.setCourseModule(m1);
        l12.setTitle("Control Flow");
        l12.setContent("if/else, switch, loops");

        m1.getLessons().add(l11);
        m1.getLessons().add(l12);

        course.getCourseModules().add(m1);

        course = courseRepo.saveAndFlush(course); // cascade persist for modules/lessons

        assertThat(course.getId()).isNotNull();
        assertThat(course.getCourseModules()).hasSize(1);
        var persistedModule = course.getCourseModules().get(0);
        assertThat(persistedModule.getId()).isNotNull();
        assertThat(persistedModule.getLessons()).hasSize(2);
        assertThat(persistedModule.getLessons().get(0).getId()).isNotNull();
        assertThat(persistedModule.getLessons().get(1).getId()).isNotNull();
    }

    // -------------------- READ (integrity) --------------------

    @Test
    void readEnrollmentIntegrity_ok() {
        var category = categoryRepo.saveAndFlush(newCategory("Databases"));
        var teacher  = userRepo.saveAndFlush(newUser("Dr. SQL", uniqueEmail("sql"), UserRole.TEACHER));
        var student  = userRepo.saveAndFlush(newUser("Student A", uniqueEmail("stuA"), UserRole.STUDENT));

        var course = new Course();
        course.setTitle("PostgreSQL Essentials " + uniq());
        course.setDescription("DDL, DML, indexes");
        course.setCategory(category);
        course.setTeacher(teacher);
        course = courseRepo.saveAndFlush(course);

        var e = new Enrollment();
        e.setStudent(student);
        e.setCourse(course);
        e.setStatus(EnrollmentStatus.ACTIVE);
        e.setEnrolledAt(Instant.now());
        e = enrollmentRepo.saveAndFlush(e);

        var found = enrollmentRepo.findById(e.getId()).orElseThrow();
        assertThat(found.getStudent().getId()).isEqualTo(student.getId());
        assertThat(found.getCourse().getId()).isEqualTo(course.getId());
        assertThat(found.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
    }

    // -------------------- UPDATE (course + submission) --------------------

    @Test
    void updateCourseAndSubmission() {
        // prepare structure
        var category = categoryRepo.saveAndFlush(newCategory("Java"));
        var teacher  = userRepo.saveAndFlush(newUser("Mentor", uniqueEmail("mentor"), UserRole.TEACHER));
        var student  = userRepo.saveAndFlush(newUser("Student B", uniqueEmail("stuB"), UserRole.STUDENT));

        var course = new Course();
        course.setTitle("Streams & Lambdas " + uniq());
        course.setDescription("Initial desc");
        course.setCategory(category);
        course.setTeacher(teacher);

        var m = new CourseModule();
        m.setCourse(course);
        m.setTitle("Streams");
        m.setOrderIndex(1);

        var lesson = new Lesson();
        lesson.setCourseModule(m);
        lesson.setTitle("Stream API Basics");
        lesson.setContent("map/filter/reduce");

        m.getLessons().add(lesson);
        course.getCourseModules().add(m);

        course = courseRepo.saveAndFlush(course);
        Long courseId = course.getId();
        Long lessonId = course.getCourseModules().get(0).getLessons().get(0).getId();

        // --- UPDATE course.description ---
        var loaded = courseRepo.findById(courseId).orElseThrow();
        loaded.setDescription("Updated description");
        courseRepo.saveAndFlush(loaded);

        var reloaded = courseRepo.findById(courseId).orElseThrow();
        assertThat(reloaded.getDescription()).isEqualTo("Updated description");

        // --- Create Assignment on a MANAGED lesson (fix for transient association) ---
        var managedLesson = lessonRepo.findById(lessonId).orElseThrow();

        var a = new Assignment();
        a.setLesson(managedLesson);
        a.setTitle("Refactor loops");
        a.setDescription("Replace loops with streams where appropriate");
        a.setMaxScore(100);
        a = assignmentRepo.saveAndFlush(a);

        // --- Submit and then grade ---
        var s = new Submission();
        s.setAssignment(a);
        s.setStudent(student);
        s.setContent("My solution using stream() and collectors");
        s.setSubmittedAt(Instant.now());
        s = submissionRepo.saveAndFlush(s);

        s.setScore(95);
        s.setFeedback("Well done, concise");
        submissionRepo.saveAndFlush(s);

        var graded = submissionRepo.findById(s.getId()).orElseThrow();
        assertThat(graded.getScore()).isEqualTo(95);
        assertThat(graded.getFeedback()).isEqualTo("Well done, concise");
    }

    // -------------------- DELETE (cascade) --------------------

    @Test
    void deleteCourse_cascadesModulesAndLessons() {
        var category = categoryRepo.saveAndFlush(newCategory("Algorithms"));
        var teacher  = userRepo.saveAndFlush(newUser("Algo Teacher", uniqueEmail("algo"), UserRole.TEACHER));

        var course = new Course();
        course.setTitle("Algorithms 101 " + uniq());
        course.setDescription("Greedy, DP, graphs");
        course.setCategory(category);
        course.setTeacher(teacher);

        var m = new CourseModule();
        m.setCourse(course);
        m.setTitle("Graphs");
        m.setOrderIndex(1);

        var l = new Lesson();
        l.setCourseModule(m);
        l.setTitle("BFS & DFS");
        l.setContent("Traversal, complexity");

        m.getLessons().add(l);
        course.getCourseModules().add(m);

        course = courseRepo.saveAndFlush(course);

        Long courseId = course.getId();
        Long moduleId = course.getCourseModules().get(0).getId();
        Long lessonId = course.getCourseModules().get(0).getLessons().get(0).getId();

        // delete root
        courseRepo.deleteById(courseId);
        courseRepo.flush();

        assertThat(courseRepo.findById(courseId)).isEmpty();
        assertThat(moduleRepo.findById(moduleId)).isEmpty();
        assertThat(lessonRepo.findById(lessonId)).isEmpty();
    }

    // -------------------- LAZY LOADING --------------------

    @Test
    void lazyLoading_shouldThrow() {
        // Build minimal course with module+lesson
        var category = categoryRepo.saveAndFlush(newCategory("CS"));
        var teacher  = userRepo.saveAndFlush(newUser("Prof. CS", uniqueEmail("cs"), UserRole.TEACHER));

        var course = new Course();
        course.setTitle("CS Basics " + uniq());
        course.setDescription("Memory, CPU, I/O");
        course.setCategory(category);
        course.setTeacher(teacher);

        var m = new CourseModule();
        m.setCourse(course);
        m.setTitle("Memory");
        m.setOrderIndex(1);

        var l = new Lesson();
        l.setCourseModule(m);
        l.setTitle("Stack vs Heap");
        l.setContent("JVM memory model overview");

        m.getLessons().add(l);
        course.getCourseModules().add(m);

        course = courseRepo.saveAndFlush(course);
        Long courseId = course.getId();

        // Без транзакции и при open-in-view:false доступ к LAZY коллекции вне запроса -> LazyInitializationException
        var detached = courseRepo.findById(courseId).orElseThrow();
        assertThatThrownBy(() -> detached.getCourseModules().size())
                .isInstanceOf(LazyInitializationException.class);
    }

    // -------------------- helpers --------------------

    private static Category newCategory(String name) {
        var c = new Category();
        c.setName(name + " " + uniq());
        return c;
    }

    private static User newUser(String name, String email, UserRole role) {
        var u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setRole(role);
        return u;
    }

    private static String uniqueEmail(String prefix) {
        return prefix + "+" + uniq() + "@test.local";
    }

    private static String uniq() {
        return Long.toString(System.nanoTime());
    }
}