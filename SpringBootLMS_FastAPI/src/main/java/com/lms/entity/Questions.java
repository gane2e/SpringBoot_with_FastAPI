package com.lms.entity;

import com.lms.dto.QuestionDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "questions")
@Getter
@Setter
@ToString
public class Questions {

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long questionId;
    
    private String questionTitle; //문제 제목

    private String option1; //문제내용 1
    private String option2; //문제내용 2
    private String option3; //문제내용 3
    private String option4; //문제내용 4
    private int correctAnswer; //문제 정답


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

}
