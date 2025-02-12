package com.lms.dto;

import com.lms.entity.CourseHashTag;
import com.lms.entity.Courses;
import com.lms.entity.StudentCourse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class HashTagFormDto {

    private Long courseHashTagId;
    private String hashTagName;
    private long course_id;

    private static ModelMapper modelMapper = new ModelMapper();

    public CourseHashTag createHashTag() {
        return modelMapper.map(this, CourseHashTag.class);
    }
    // CourseHashTag => StudentCourseDto 변환
    public static HashTagFormDto of(CourseHashTag hashTag) {
        return modelMapper.map(hashTag, HashTagFormDto.class);
    }
}
