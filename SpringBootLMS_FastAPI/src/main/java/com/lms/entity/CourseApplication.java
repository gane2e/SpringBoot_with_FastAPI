package com.lms.entity;

import com.lms.constant.Application_status;
import com.lms.constant.Course_status;
import com.lms.constant.Recruitment_status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "course_application")
@Getter
@Setter
public class CourseApplication {

    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Courses course;

    /* 신청상태 */
    @Enumerated(EnumType.STRING) //
    private Application_status application_status;
}