package com.lms.service;

import com.lms.constant.Completion_status;
import com.lms.constant.Enrollment_status;
import com.lms.constant.Test_status;
import com.lms.dto.StudentCourseDto;
import com.lms.dto.StudentCourseHisDto;
import com.lms.entity.StudentCourse;
import com.lms.entity.StudentTest;
import com.lms.repository.StudentCourseRepository;
import com.lms.repository.StudentTestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;
    private final StudentTestRepository studentTestRepository;
    private AtomicInteger count = new AtomicInteger(1); //수료번호 생성하기


    // 영상학습페이지 - 수강정보 반환
    public StudentCourseDto findByApplicationId(Long applicationId) {
        StudentCourse student = studentCourseRepository.findByCourseApplication_ApplicationId(applicationId);
        StudentCourseDto studentCourseDto = StudentCourseDto.of(student);
        return studentCourseDto;
    }

    // 수강생 마지막 시청시간(초) 실시간저장
    public void saveLastWatched(Long studentCourseId, Long last_watched, Enrollment_status enrollment_status,  double ProgressRate) {
        StudentCourse studentCourse = studentCourseRepository.findById(studentCourseId)
                .orElseThrow(EntityNotFoundException::new);
        studentCourse.updateLastWatched(last_watched, enrollment_status, ProgressRate);
        studentCourseRepository.save(studentCourse);
    }

    // 수강생 대시보드 요청시 회원명, 교육정보, 수강정보 반환
    public List<StudentCourseHisDto> findStudentCourseHis(Long studentCourseId) {
        List<StudentCourseHisDto> hisDtos
                = studentCourseRepository.getDashBoard(studentCourseId);
        return hisDtos;
    }
    
    // 대시보드dto에 시험응시내역 추가해서 반환하기
    public List<StudentCourseHisDto> updateTestHistory(List<StudentCourseHisDto> dtos) {
        for (StudentCourseHisDto hisDtos : dtos) {

            //특정 회원의 수강내역ID 순회하여 얻기
            Long studentCourseId = hisDtos.getStudentCourseId();
            
            //해당 수강내역 ID에 해당하는 TEST(시험응시내역)엔티티 얻어오기
            StudentTest studentTest =  studentTestRepository.findByStudentCourse_StudentCourseId(studentCourseId);

            //시험내역ID / 차시별 응시상태 및 점수 저장(1차시, 2차시, 3차시)
            hisDtos.setStudentTestId(studentTest.getStudentTestId());
            hisDtos.setFirstScore(studentTest.getFirstScore());
            hisDtos.setFirstAttemptStatus(studentTest.getFirstAttemptStatus());
            hisDtos.setSecondScore(studentTest.getSecondScore());
            hisDtos.setSecondAttemptStatus(studentTest.getSecondAttemptStatus());
            hisDtos.setThirdScore(studentTest.getThirdScore());
            hisDtos.setThirdAttemptStatus(studentTest.getThirdAttemptStatus());

            if(studentTest.getFirstAttemptStatus() == Test_status.미응시) {
                hisDtos.setLastStatus(studentTest.getFirstAttemptStatus());

            } else if (studentTest.getSecondAttemptStatus() == Test_status.미응시 ) {
                hisDtos.setLastScore(studentTest.getFirstScore());
                hisDtos.setLastStatus(studentTest.getFirstAttemptStatus());
                hisDtos.setTestCount(1);
            } else if (studentTest.getThirdAttemptStatus() == Test_status.미응시){
                hisDtos.setLastScore(studentTest.getSecondScore());
                hisDtos.setLastStatus(studentTest.getSecondAttemptStatus());
                hisDtos.setTestCount(2);
            } else {
                hisDtos.setLastScore(studentTest.getThirdScore());
                hisDtos.setLastStatus(studentTest.getThirdAttemptStatus());
                hisDtos.setTestCount(3);
            }
        }
        return dtos;
    }
    
    // 수료처리하기
    public void certificationSave(Long studentCourseId){

        StudentCourse studentCourse = studentCourseRepository.findById(studentCourseId).orElseThrow();
       
        studentCourse.setCompletionStatus(Completion_status.수료); //수료처리
        studentCourse.setCompletionDateTime(LocalDateTime.now());

        //수료번호 생성(현재연도+4자리숫자 수료순서(20250001,20250002..))
        studentCourse.setCertificationNumber(generateCertificateNumber());

        studentCourseRepository.save(studentCourse);
    }

    // 수료번호 생성하기
    public String generateCertificateNumber() {
        
        int currentYear = LocalDate.now().getYear();
        int currentCount = count.getAndIncrement();
        
        return String.format("%d%04d", currentYear, currentCount); // 4자리로 0을 채운 카운트와 연도 결합
    }


    

}
