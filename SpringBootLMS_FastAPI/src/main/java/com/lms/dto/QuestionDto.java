package com.lms.dto;

import com.lms.entity.Questions;
import com.lms.entity.StudentCourse;
import com.lms.entity.SubCategory;
import com.lms.entity.Videos;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class QuestionDto {

    private Long questionId;

    private String questionTitle; //문제 제목

    private String option1; //문제내용 1
    private String option2; //문제내용 2
    private String option3; //문제내용 3
    private String option4; //문제내용 4
    private int correctAnswer; //문제 정답

    private Long subCategoryId;

    private static ModelMapper modelMapper = new ModelMapper();
    public Questions createQuestion() {
        return modelMapper.map(this, Questions.class);
    }

    // Questions => QuestionDto 변환
    public static QuestionDto of(Questions questions) {
        return modelMapper.map(questions, QuestionDto.class);
    }
}
