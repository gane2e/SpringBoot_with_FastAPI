package com.lms.dto;

import com.lms.constant.Course_status;
import com.lms.constant.Recruitment_status;
import com.lms.entity.Courses;
import com.lms.entity.Videos;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CourseFormDto {

    /* 교육과정 DTO */
    private Long courseId;

    @NotBlank(message = "교육과정 제목은 필수 입력값입니다.")
    private String title; /* 교육과정 제목 */
    
    @NotBlank(message = "교육과정 소개는 필수 입력값입니다.")
    private String description; /* 교육 소개 */
    
    /* 모집상태 */
    private Recruitment_status recruitment_status; //모집상태
    private LocalDate recruitment_start_date; //모집시작일(선택)
    private LocalDate recruitment_end_date; //모집종료일(선택)
    /* 교육상태 */
    private Course_status course_status; //교육상태
    private LocalDate course_start_date; //교육시작일(선택)
    private LocalDate course_end_date; //교육종료일(선택)

    @NotBlank(message = "수료기준은 필수 입력값입니다.")
    private String completionCriteria; /* 수료기준 */

    /* 썸네일처리 */
    private String imgName; /* 영상 파일명 */
    private String oriImgName; /* 썸네일 원본 이름 */
    private String imgUrl; /* 썸네일 조회 경로 */
    
    @NotNull(message = "서브 카테고리 값이 비어있습니다.")
    private Long subCategoryId; //해당 교육과정의 카테고리id

    private static ModelMapper modelMapper = new ModelMapper();

    /* CourseFormDto -> Courses 변환 */
    public Courses createCourse() {
        return modelMapper.map(this, Courses.class);
    }

    /* Courses -> CourseFormDto 변환 */
    public static CourseFormDto of(Courses courses) {
        return modelMapper.map(courses, CourseFormDto.class);
    }

}
