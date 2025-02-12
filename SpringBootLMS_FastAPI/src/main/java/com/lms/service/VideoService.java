package com.lms.service;

import com.lms.dto.VideoFormDto;
import com.lms.dto.VideoListDto;
import com.lms.entity.Videos;
import com.lms.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class VideoService {

    /* 비디오 CRUD 구현 */
    private final VideoRepository videoRepository;

    public List<VideoFormDto> findVideosBySubCategoryId(Long subCategoryId) {

        // 관리자 > 교육등록 > 카테고리선택 > 선택한 카테고리의 교육영상 등록일기준 내림차순 찾기
        List<Videos> videos = videoRepository.findBySubCategory_SubCategoryId(subCategoryId);

        /* videos -> videoFormDto 로 변환함. */
        List<VideoFormDto> VideoFormDtoList = new ArrayList<>();
        for(Videos video : videos){
            log.info("videoFormDto로 변환중인 video엔티티.... ==> " + video);
            VideoFormDto videoFormDto = VideoFormDto.of(video);
            VideoFormDtoList.add(videoFormDto);
        }

        return VideoFormDtoList; // Spring Data JPA를 사용하는 경우

    }

    // 관리자 > 영상목록 > 영상정보 + 소속 카테고리정보 추출
    public List<VideoListDto> findAllVideosByCategoryInfo() {
        return videoRepository.findVideoWithCategoryInfo();
    }
}
