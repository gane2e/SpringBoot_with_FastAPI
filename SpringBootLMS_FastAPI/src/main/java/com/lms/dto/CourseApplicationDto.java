package com.lms.dto;

import com.lms.constant.Application_status;
import com.lms.constant.Recruitment_status;
import com.lms.entity.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class CourseApplicationDto {

    /* 교육 신청내역 DTO */
    private Long applicationId;
    private Long memberId;
    private Long courseId;
    private Application_status application_status;

    private static ModelMapper modelMapper = new ModelMapper();

    /* Courses -> CourseFormDto 변환 */
    public static CourseApplicationDto of(CourseApplication application) {
        return modelMapper.map(application, CourseApplicationDto.class);
    }

}
