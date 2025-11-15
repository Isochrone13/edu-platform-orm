package com.example.edu.repository;

import com.example.edu.domain.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    // лёгкий список по учителю/категории
    Page<Course> findByTeacherId(Long teacherId, Pageable pageable);
    Page<Course> findByCategory_Name(String categoryName, Pageable pageable);

    // подгружаем преподавателя и категорию одним запросом (динамический граф)
    @EntityGraph(attributePaths = {"teacher", "category"})
    Optional<Course> findById(Long id);

    // структура: модули + уроки (динамический граф с "точечной" нотацией)
    @EntityGraph(attributePaths = {"courseModules", "courseModules.lessons"})
    @Query("select c from Course c where c.id = :id")
    Optional<Course> fetchStructureById(@Param("id") Long id);

    // простой топ по категории
    List<Course> findTop10ByCategory_NameOrderByIdDesc(String categoryName);

    @Query("select count(e) from Course c join c.enrollments e where c.id = :courseId")
    long studentCount(@Param("courseId") Long courseId);
}
