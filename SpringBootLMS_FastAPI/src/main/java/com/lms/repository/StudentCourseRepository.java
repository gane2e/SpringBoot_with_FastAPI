package com.lms.repository;

import com.lms.dto.StudentCourseHisDto;
import com.lms.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    StudentCourse findByCourseApplication_ApplicationId(Long applicationId);

    // 사용자 대시보드
    @Query("SELECT new com.lms.dto.StudentCourseHisDto(a.applicationId, a.application_status, " +
            "s.studentCourseId, s.ProgressRate, s.completionDateTime, s.completionStatus, s.courseStarDateTime, " +
            "s.enrollmentStatus, s.regTime, " +
            "m.id, m.name, " +
            "c.courseId , c.title ," +
            "sct.subCategoryName, sct.subCategoryId," +
            "ct.categoryName, s.certificationNumber) " +
            "FROM StudentCourse s " +
            "JOIN s.courseApplication a " +
            "JOIN a.member m " +
            "JOIN a.course c " +
            "JOIN c.subCategory sct " +
            "JOIN sct.categories ct " +
            "WHERE a.member.id = :memberId " +
            "order by s.regTime desc")
    List<StudentCourseHisDto> getDashBoard(@RequestParam("memberId") Long memberId);

}
