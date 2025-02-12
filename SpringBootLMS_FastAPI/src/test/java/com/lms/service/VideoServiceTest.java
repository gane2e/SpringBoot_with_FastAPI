package com.lms.service;

import com.lms.dto.VideoFormDto;
import com.lms.dto.VideoListDto;
import com.lms.entity.Videos;
import com.lms.repository.VideoRepository;
import groovy.util.logging.Log4j2;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Log4j2
class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Test
    void VideoByCategoryTest() {

       List<VideoFormDto> result = videoService.findVideosBySubCategoryId(2L);

        System.out.println("Test VideoByCategory");

        result.forEach(videoFormDto -> {
            System.out.println("Video ID: " + videoFormDto.getVideoId());
            System.out.println("Video Title: " + videoFormDto.getTitle());
            System.out.println("SubCategory ID: " + videoFormDto.getSubCategoryId());
            System.out.println("---------------------------");
        });
    }

    @Test
    void findAllVideosByCategoryInfoTest() {
        List<VideoListDto> result = videoService.findAllVideosByCategoryInfo();

        result.forEach(VideoListDto -> {
            System.out.println("Video categoryName: " + VideoListDto.getCategoryName());
            System.out.println("Video subCategoryName: " + VideoListDto.getSubCategoryName());
            System.out.println("Video title: " + VideoListDto.getTitle());
            System.out.println("Video oriVideoName: " + VideoListDto.getOriVideoName());
            System.out.println("Video videoId: " + VideoListDto.getVideoId());
            System.out.println("Video regTime: " + VideoListDto.getRegTime());
            System.out.println("Video updateTime: " + VideoListDto.getUpdateTime());
            System.out.println("Video createdBy: " + VideoListDto.getCreatedBy());
            System.out.println("Video modifiedBy: " + VideoListDto.getModifiedBy());
            System.out.println("---------------------------");
        });
    }



}