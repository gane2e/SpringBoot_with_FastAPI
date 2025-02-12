package com.lms.dto;

import com.lms.constant.Test_status;
import com.lms.entity.StudentTest;
import com.lms.entity.Videos;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class StudentTestDto {

    private Long studentTestId;

    private int firstScore; //1차점수
    private Test_status firstAttemptStatus; //1차 응시 상태

    private int secondScore; //2차점수
    private Test_status secondAttemptStatus; //2차 응시 상태

    private int thirdScore; //3차점수
    private Test_status thirdAttemptStatus; //3차 응시 상태

    private boolean isReset; // 리셋여부

    private Long studentCourseId; //수강생Id

    private static ModelMapper modelMapper = new ModelMapper();

    public StudentTest createTestEntity(StudentTestDto studentTestDto) {
        return modelMapper.map(this, StudentTest.class);
    }

    /* studentTest -> StudentTestDto 변환 */
    public static StudentTestDto of(StudentTest studentTest) {
        return modelMapper.map(studentTest, StudentTestDto.class);
    }
}
