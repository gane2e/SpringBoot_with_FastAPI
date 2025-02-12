package com.lms.repository;

import com.lms.entity.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentTestRepository extends JpaRepository<StudentTest, Long> {

    StudentTest findByStudentCourse_StudentCourseId(Long studentCourseId);
}
