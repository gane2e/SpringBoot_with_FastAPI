package com.lms.service;

import com.lms.dto.CourseVideoDto;
import com.lms.entity.CourseVideo;
import com.lms.entity.Videos;
import com.lms.repository.CourseVideoRepository;
import com.lms.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class CourseVideoService {
    
    /* 교육과정에 해당하는 영상목록 처리 */
    private final CourseVideoRepository courseVideoRepository;

    private final VideoRepository videoRepository;

    public void saveCourseVideo(CourseVideoDto courseVideoDto) {

        // videoId로 연관관계 비디오 객체 구하기
        Videos video = videoRepository.findById(courseVideoDto.getVideoId())
                .orElseThrow( () -> new EntityNotFoundException());


        // 영상 순서 구하기
        Long courseVideoIndex = courseVideoDto.getCourseVideoIndex();
        CourseVideo courseVideo = CourseVideo.createCourseVideo(courseVideoIndex,video);
        courseVideoRepository.save(courseVideo);


    }
}
