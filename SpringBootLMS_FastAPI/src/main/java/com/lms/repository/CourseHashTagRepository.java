package com.lms.repository;

import com.lms.dto.HashTagCountDto;
import com.lms.entity.CourseHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseHashTagRepository extends JpaRepository<CourseHashTag, Long> {

    List<CourseHashTag> findByCourseIsNull();

    List<CourseHashTag> findByCourse_CourseId(Long courseId);

    @Query("SELECT new com.lms.dto.HashTagCountDto(COUNT(ch), ch.hashTagName) " +
            "FROM CourseHashTag ch " +
            "GROUP BY ch.hashTagName " +
            "ORDER BY COUNT(ch) DESC " +
            "limit 5")
    List<HashTagCountDto> findByTop5Hashtags();
}
