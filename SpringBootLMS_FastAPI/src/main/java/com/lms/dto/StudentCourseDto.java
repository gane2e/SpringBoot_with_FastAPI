package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lms.constant.Completion_status;
import com.lms.constant.Enrollment_status;
import com.lms.constant.Test_status;
import com.lms.entity.StudentCourse;
import com.lms.entity.Videos;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudentCourseDto {

    @JsonProperty("studentCourseId")
    private Long studentCourseId;

    private LocalDateTime courseStarDateTime; //수강 시작일

    private LocalDateTime completionDateTime; //학습 수료일

    @JsonProperty("ProgressRate")
    private double ProgressRate; // 진도율

    @JsonProperty("last_watched")
    private Long last_watched; //마지막 시청시간

    //수강상태(수강신청, 학습중, 학습완료)
    private Enrollment_status enrollmentStatus;

    //수료상태(수료, 미수료)
    private Completion_status completionStatus;

    //신청내역 외래키
    private Long applicationId;

    private static ModelMapper modelMapper = new ModelMapper();

    public StudentCourse createStudentCourse() {
        return modelMapper.map(this, StudentCourse.class);
    }
    
    // StudentCourse => StudentCourseDto 변환
    public static StudentCourseDto of(StudentCourse studentCourse) {
        return modelMapper.map(studentCourse, StudentCourseDto.class);
    }

}
