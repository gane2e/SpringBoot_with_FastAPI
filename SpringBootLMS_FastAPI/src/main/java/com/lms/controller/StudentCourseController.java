package com.lms.controller;

import com.lms.constant.Enrollment_status;
import com.lms.dto.StudentCourseDto;
import com.lms.entity.StudentCourse;
import com.lms.repository.StudentCourseRepository;
import com.lms.service.StudentCourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/student/")
public class StudentCourseController {

    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    StudentCourseRepository studentCourseRepository;

    // 사용자 마지막 시청시간 실시간저장
    @PostMapping(value = "/lastWatchedSave")
    public ResponseEntity<String> lastWatchedSave(@RequestBody StudentCourseDto studentCourseDto){

        Long studentCourseId = studentCourseDto.getStudentCourseId(); //수강생ID
        Long last_watched = studentCourseDto.getLast_watched(); // 마지막시청초
        Enrollment_status enrollment_status = studentCourseDto.getEnrollmentStatus(); //학습상태
        double ProgressRate = studentCourseDto.getProgressRate(); //진도율

        studentCourseService.saveLastWatched
                (studentCourseId, last_watched, enrollment_status, ProgressRate);

        return ResponseEntity.ok("마지막 시청시간 저장완료");
    }

    
    //시험보기 클릭시 [학습완료] 상태인지 체크
    @PostMapping(value = "/checkCourseStatus")
    public ResponseEntity<String> checkCourseStatus(@RequestBody StudentCourseDto studentCourseDto){
        Long studentCourseId = studentCourseDto.getStudentCourseId(); //수강생ID
        StudentCourse student = studentCourseRepository.findById(studentCourseId)
                .orElseThrow();
        if (student.getEnrollmentStatus() == Enrollment_status.학습완료) {
            return ResponseEntity.ok("학습완료");
        } else {
            return ResponseEntity.status(401).body("학습중");
        }
    }

    //수료증 발급
    @GetMapping(value = "/certificate")
    public String certificate(){
        return "course/certificate_of_completion";
    }
}
