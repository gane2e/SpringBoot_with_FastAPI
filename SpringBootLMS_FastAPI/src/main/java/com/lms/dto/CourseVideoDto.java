package com.lms.dto;

import com.lms.entity.CourseVideo;
import com.lms.entity.Courses;
import com.lms.entity.Videos;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class CourseVideoDto {

    private Long courseVideoId; //교육에 등록된 영상목록 기본키

    private Long courseId; //교육과정 외래키
    
    private Long videoId; //영상 외래키

    private Long courseVideoIndex; /* 교육 내 영상 목차 순서 */

    private String videoTitle; /* 영상 제목 */

    private String videoName; /* 영상 파일명 */

    private String oriVideoName; /* 영상파일 원본 이름 */

    private String videoUrl; /* 영상파일 조회 경로 */

    private String courseTitle; /* 영상파일 조회 경로 */

    private static ModelMapper modelMapper = new ModelMapper();

    /* CourseVideoDto -> CourseVideo 변환 */
    public CourseVideo createCourseVideo() {
        return modelMapper.map(this, CourseVideo.class);
    }

    /* CourseVideo -> CourseVideoDto 변환 */
    public static CourseVideoDto of(CourseVideo courseVideo) {
        return modelMapper.map(courseVideo, CourseVideoDto.class);
    }
    
    
    // 기본생성자
    public CourseVideoDto() {}

    // 교육과정에 해당하는 영상 표출용
    public CourseVideoDto(Long courseId, Long videoId, Long courseVideoIndex,
                          String videoTitle, String  videoName,String oriVideoName, String videoUrl,
                          String courseTitle) {
        this.courseId = courseId;
        this.videoId = videoId;
        this.courseVideoIndex = courseVideoIndex;
        this.videoTitle = videoTitle;
        this.videoName = videoName;
        this.oriVideoName = oriVideoName;
        this.videoUrl = videoUrl;
        this.courseTitle = courseTitle;
    }
}
