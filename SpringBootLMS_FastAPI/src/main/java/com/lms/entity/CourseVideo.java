package com.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lms.dto.CourseVideoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "course_video")
@Getter
@Setter
public class CourseVideo extends BaseEntity {

    @Id
    @Column(name = "course_video_id")
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long courseVideoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    /*@JsonIgnore // 순환 참조 방지 (필요한 경우), 양방향 참조시 무한루프 발생가능*/
    private Courses courses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Videos videos;

    private Long courseVideoIndex; /* 교육 내 영상 목차 순서 */


    public static CourseVideo createCourseVideo(Long courseVideoIndex, Videos video) {
        CourseVideo courseVideo = new CourseVideo();
        courseVideo.setVideos(video);
        courseVideo.setCourseVideoIndex(courseVideoIndex);
        return courseVideo;
    }

    public void updatecourseId(Courses course){
        this.courses = course;
    }








}
