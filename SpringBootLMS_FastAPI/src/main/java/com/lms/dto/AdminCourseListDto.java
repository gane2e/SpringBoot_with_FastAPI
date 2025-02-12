package com.lms.dto;

import com.lms.constant.Recruitment_status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminCourseListDto {

    private Long courseId; //교육과정 ID
    private Recruitment_status recruitment_status; //모집상태
    private String courseTitle; // 교육과정 제목
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private String createdBy;
    private String modifiedBy;
    private String imgName; /* 썸네일 조회 경로 */
    private String imgUrl; /* 썸네일 조회 경로 */
    private String oriImgName; /* 썸네일 원본 이름 */
    private int numberOfApplications;
    private String subCategoryName;
    private String categoryName;
    private double completionRate;

    // 생성자
    public AdminCourseListDto(Long courseId, Recruitment_status recruitment_status, String courseTitle, LocalDateTime regTime,
                  LocalDateTime updateTime, String createdBy, String modifiedBy, String imgName, String imgUrl,
                  String oriImgName, int numberOfApplications, String subCategoryName, String categoryName) {
        this.courseId = courseId;
        this.recruitment_status = recruitment_status;
        this.courseTitle = courseTitle;
        this.regTime = regTime;
        this.updateTime = updateTime;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
        this.oriImgName = oriImgName;
        this.numberOfApplications = numberOfApplications;
        this.subCategoryName = subCategoryName;
        this.categoryName = categoryName;
    }



}
