package com.lms.service;

import com.lms.dto.CourseApplicationDto;
import com.lms.entity.CourseApplication;
import com.lms.repository.CourseApplicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ApplicationService {

    private final CourseApplicationRepository courseApplicationRepository;

    // 멤버 고유키로 신청내역 dto 반환하기
    // XXX 신청내역 고유키로 반환해야함.
    public CourseApplicationDto findByApplicationId(Long applicationId) {
        CourseApplication application = courseApplicationRepository.findByApplicationId(applicationId);
        CourseApplicationDto dto = CourseApplicationDto.of(application);
        return dto;
    }
}
