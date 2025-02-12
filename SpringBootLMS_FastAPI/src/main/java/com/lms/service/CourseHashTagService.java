package com.lms.service;

import com.lms.dto.CourseListDto;
import com.lms.dto.HashTagCountDto;
import com.lms.dto.HashTagFormDto;
import com.lms.entity.CourseHashTag;
import com.lms.entity.StudentCourse;
import com.lms.repository.CourseHashTagRepository;
import com.lms.repository.StudentCourseRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class CourseHashTagService {

    private final CourseHashTagRepository courseHashTagRepository;

    //교육등록시 해시태그 저장
    public void saveHashTag(List<HashTagFormDto> hashTagFormDtos){

        hashTagFormDtos.forEach(hashTagFormDto -> {
            CourseHashTag courseHashTag = hashTagFormDto.createHashTag();
            courseHashTagRepository.save(courseHashTag);
        });
    }

    //교육과정에 해당하는 해시태그 리스트 찾기
    public List<HashTagFormDto>  getHashTagFormDto(Long courseId){

        List<CourseHashTag> hashTagList = courseHashTagRepository.findByCourse_CourseId(courseId);
        List<HashTagFormDto> hashTagFormDtoList = new ArrayList<>();
        hashTagList.forEach(hashTag -> {
            HashTagFormDto dto = HashTagFormDto.of(hashTag);
            hashTagFormDtoList.add(dto);
        });
        return hashTagFormDtoList;
    }

    //해시태그 많이 등록된 순으로 5개 조회하기
    public List<HashTagCountDto> findByTop5Hashtags(){
        return courseHashTagRepository.findByTop5Hashtags();
    }


    
}
