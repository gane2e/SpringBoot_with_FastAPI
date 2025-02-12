package com.lms.repository;


import com.lms.dto.AdminCourseListDto;
import com.lms.dto.CourseListDto;
import com.lms.dto.CourseVideoDto;
import com.lms.dto.VideoListDto;
import com.lms.entity.Courses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Courses, Long> {


    // 사용자 페이지 - 교육과정 리스트
    @Query("SELECT new com.lms.dto.CourseListDto(co.courseId, co.title, co.description, co.recruitment_status, " +
            "co.recruitment_start_date, co.recruitment_end_date, co.course_status, co.course_start_date, co.course_end_date, " +
            "co.completionCriteria, co.regTime, co.updateTime, co.createdBy, co.modifiedBy, " +
            "co.imgUrl, co.oriImgName, ct.categoryId, ct.categoryName, sct.subCategoryId, sct.subCategoryName, co.numberOfApplications, co.totalRating, co.ratingCount) " +
            "FROM Courses co " +
            "JOIN co.subCategory sct " +
            "JOIN sct.categories ct " +
            "WHERE (:keyword IS NULL OR LOWER(co.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(co.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:categoryId IS NULL OR ct.categoryId = :categoryId) " +
            "AND (:subCategoryId IS NULL OR sct.subCategoryId = :subCategoryId) " +
            "ORDER BY co.regTime DESC")
    Page<List<CourseListDto>> findAllCourseWithCategoryInfo(@Param("keyword") String keyword,
                                                      @Param("categoryId") Long categoryId,
                                                      @Param("subCategoryId") Long subCategoryId,
                                                            Pageable pageable);

    // 사용자 페이지 - 교육 상세정보 표출용
    @Query("SELECT new com.lms.dto.CourseListDto(co.courseId, co.title, co.description, co.recruitment_status, " +
            "co.recruitment_start_date, co.recruitment_end_date, co.course_status, co.course_start_date, co.course_end_date, " +
            "co.completionCriteria, co.regTime, co.updateTime, co.createdBy, co.modifiedBy, " +
            "co.imgUrl, co.oriImgName, ct.categoryId , ct.categoryName , sct.subCategoryId, sct.subCategoryName, co.numberOfApplications, co.totalRating, co.ratingCount) " +
            "FROM Courses co " +
            "JOIN co.subCategory sct " +
            "JOIN sct.categories ct " +
            "WHERE co.courseId = :courseId")
    CourseListDto findCourseById(@Param("courseId") Long courseId);


    // 사용자 페이지 - 특정교육 교육영상 반환
    @Query("SELECT new com.lms.dto.CourseVideoDto" +
            "(c.courseId , v.videoId , cv.courseVideoIndex, v.title, " +
            "v.videoName, v.oriVideoName, v.videoUrl, c.title) " +
            "FROM CourseVideo cv " +
            "JOIN cv.videos v " +
            "JOIN cv.courses c " +
            "WHERE cv.courses.courseId = :courseId")
    List<CourseVideoDto> findVideoById(@Param("courseId") Long courseId);


    // 사용자 페이지 - 메인비주얼 교육 최근등록 5개
    @Query("SELECT new com.lms.dto.CourseListDto(co.courseId, co.title, co.description, co.recruitment_status, " +
            "co.recruitment_start_date, co.recruitment_end_date, co.course_status, co.course_start_date, co.course_end_date, " +
            "co.completionCriteria, co.regTime, co.updateTime, co.createdBy, co.modifiedBy, " +
            "co.imgUrl, co.oriImgName, ct.categoryId, ct.categoryName, sct.subCategoryId, sct.subCategoryName, co.numberOfApplications, co.totalRating, co.ratingCount) " +
            "FROM Courses co " +
            "JOIN co.subCategory sct " +
            "JOIN sct.categories ct " +
            "ORDER BY co.regTime DESC limit 5")
    List<CourseListDto> getMainVisualList();

    // 사용자 페이지 - 메인 교육리스트 신청자많은순 8개
    @Query("SELECT new com.lms.dto.CourseListDto(co.courseId, co.title, co.description, co.recruitment_status, " +
            "co.recruitment_start_date, co.recruitment_end_date, co.course_status, co.course_start_date, co.course_end_date, " +
            "co.completionCriteria, co.regTime, co.updateTime, co.createdBy, co.modifiedBy, " +
            "co.imgUrl, co.oriImgName, ct.categoryId, ct.categoryName, sct.subCategoryId, sct.subCategoryName, co.numberOfApplications, co.totalRating, co.ratingCount) " +
            "FROM Courses co " +
            "JOIN co.subCategory sct " +
            "JOIN sct.categories ct " +
            "ORDER BY co.numberOfApplications DESC, co.regTime DESC limit 8")
    List<CourseListDto> top8CourseList();


    // 관리자 페이지 - 교육과정 관리 리스트
    @Query("SELECT new com.lms.dto.AdminCourseListDto" +
            "(s.courseId , s.recruitment_status , s.title, s.regTime, " +
            "s.updateTime, s.createdBy, s.modifiedBy, s.imgName, s.imgUrl, s.oriImgName, " +
            "s.numberOfApplications, sct.subCategoryName, ct.categoryName) " +
            "FROM Courses s " +
            "JOIN s.subCategory sct " +
            "JOIN sct.categories ct " +
            "order by s.regTime desc")
    List<AdminCourseListDto> findAllAdminCourseList();


    @Query("SELECT new com.lms.dto.CourseListDto(co.courseId, co.title, co.description, co.recruitment_status, " +
            "co.recruitment_start_date, co.recruitment_end_date, co.course_status, co.course_start_date, co.course_end_date, " +
            "co.completionCriteria, co.regTime, co.updateTime, co.createdBy, co.modifiedBy, " +
            "co.imgUrl, co.oriImgName, ct.categoryId, ct.categoryName, sct.subCategoryId, sct.subCategoryName, co.numberOfApplications, co.totalRating, co.ratingCount) " +
            "FROM Courses co " +
            "JOIN co.subCategory sct " +
            "JOIN sct.categories ct " +
            "WHERE co.courseId = :courseId")
    CourseListDto findAllByInCourseId(@Param("courseId") Long courseId);
}
