package com.lms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "course_hashtag")
@Getter
@Setter
public class CourseHashTag extends BaseTimeEntity{

    @Id
    @Column(name = "course_hashtag_id")
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long courseHashTagId;

    private String hashTagName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Courses course;

    public void updatecourseId(Courses course){
        this.course = course;
    }

}
