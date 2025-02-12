package com.lms.repository;

import com.lms.entity.CourseVideo;
import com.lms.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseVideoRepository extends JpaRepository<CourseVideo, Long> {
    List<CourseVideo> findByCoursesIsNull();
}
