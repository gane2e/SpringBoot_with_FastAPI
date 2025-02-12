package com.lms.dto;

import com.lms.constant.Application_status;
import com.lms.constant.Completion_status;
import com.lms.constant.Enrollment_status;
import com.lms.constant.Test_status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudentCourseHisDto {

    //신청내역
    private Long applicationId;
    private Application_status application_status;

    //수강내역
    private Long studentCourseId;
    private double ProgressRate; //진도율
    private LocalDateTime completionDateTime; //학습 수료일
    private LocalDateTime courseStarDateTime; //수강 시작일
    private Completion_status completionStatus; //수료상태(수료, 미수료)
    private Enrollment_status enrollmentStatus; //수강상태(수강신청, 학습중, 학습완료)
    private LocalDateTime regTime;
    
    //회원정보
    private Long memberId;
    private String memberName;
    
    //교육정보
    private Long courseId;
    private String courseTitle;

    //카테고리 정보
    private String subCategoryName;
    private Long subCategoryId;
    private String categoryName;

    //시험내역 정보
    private Long studentTestId;
    private int firstScore; //1차점수
    private Test_status firstAttemptStatus; //1차 응시 상태
    private int secondScore; //2차점수
    private Test_status secondAttemptStatus; //2차 응시 상태
    private int thirdScore; //3차점수
    private Test_status thirdAttemptStatus; //3차 응시 상태

    //대시보드에 보여질 마지막 시험응시결과
    private Test_status lastStatus;
    private int lastScore;
    private int testCount;
    
    private String certificationNumber; //수료번호

    public StudentCourseHisDto(Long applicationId, Application_status application_status,
                               Long studentCourseId, double ProgressRate, LocalDateTime completionDateTime,  Completion_status completionStatus,  LocalDateTime courseStarDateTime,
                               Enrollment_status enrollmentStatus, LocalDateTime regTime,
                               Long memberId, String memberName, Long courseId, String courseTitle, String subCategoryName,
                               Long subCategoryId, String categoryName, String certificationNumber) {

        // 신청내역
        this.applicationId = applicationId;
        this.application_status = application_status;

        // 수강내역
        this.studentCourseId = studentCourseId;
        this.ProgressRate = ProgressRate;
        this.completionDateTime = completionDateTime;
        this.completionStatus = completionStatus;
        this.courseStarDateTime = courseStarDateTime;
        this.enrollmentStatus = enrollmentStatus;
        this.regTime = regTime;

        // 회원정보
        this.memberId = memberId;
        this.memberName = memberName;

        // 교육정보
        this.courseId = courseId;
        this.courseTitle = courseTitle;

        // 카테고리 정보
        this.subCategoryName = subCategoryName;
        this.subCategoryId = subCategoryId;
        this.categoryName = categoryName;

        this.certificationNumber = certificationNumber; //수료번호

    }


}
