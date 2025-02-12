package com.lms.controller;

import com.lms.constant.Completion_status;
import com.lms.constant.Test_status;
import com.lms.entity.*;
import com.lms.repository.*;
import com.lms.service.QuestionService;
import com.lms.service.StudentCourseService;
import com.lms.service.StudentTestService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private StudentTestService studentTestService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StudentCourseRepository studentCourseRepository;
    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired
    private CourseApplicationRepository courseApplicationRepository;

    @PostMapping(value = "/question/grading/{studentTestId}/{studentCourseId}/{courseTitle}/{subCategoryId}")
    public String gradingQuestion(@RequestParam Map<String, String> answers,
                                  Model model,
                                  @PathVariable("studentTestId") Long studentTestId,
                                  @PathVariable("studentCourseId") Long studentCourseId,
                                  @PathVariable("courseTitle") String courseTitle,
                                  @PathVariable("subCategoryId") Long subCategoryId) {

        int totalScore = 0;
        int passingScore = 60;
        int totalQuestions = answers.size();
        int testCount = 0;

        // 문제의 배점 계산 (100점 / 문제 개수)
        int pointsPerQuestion = 100 / totalQuestions;

        for (Map.Entry<String, String> entry : answers.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            Long questionId = Long.parseLong(entry.getKey()); //응시한 문제
            int userAnswer = Integer.parseInt(entry.getValue()); //선택한 답
            boolean result =  questionService.checkAnswer(questionId, userAnswer);
            if (result) {
                totalScore += pointsPerQuestion;
            }
        }

        // 사용자가 선택한 문제의 정답 토탈 점수가 60점 이상 / 이하일 시 여부에 따른 합불합처리
        if(totalScore < passingScore) {
                testCount = studentTestService.saveTest(studentTestId, Test_status.불합격, totalScore);
            if(testCount == 3) {
                //불합격이면서 3회차시험인 회원의 시험응시내역 초기화하기
                studentTestService.resetTest(studentTestId, studentCourseId);
            }
            model.addAttribute("testStatus", Test_status.불합격);
        } else {
            testCount = studentTestService.saveTest(studentTestId, Test_status.합격, totalScore);
            model.addAttribute("testStatus", Test_status.합격);
            //수료처리하기
            studentCourseService.certificationSave(studentCourseId);
        }

        //로그인중인 사용자 ID로 성명 구하기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberRepository.findByLoginId(username);
        String name = member.getName();

        StudentCourse studentCourse = studentCourseRepository.findById(studentCourseId)
                .orElseThrow();
        Long applicationId = studentCourse.getCourseApplication().getApplicationId();
        
        //신청내역으로 교육정보 구하기
        CourseApplication app =
                courseApplicationRepository.findByApplicationId(applicationId);
        Long courseId = app.getCourse().getCourseId();

        //결과페이지 표출용 점수, 차시, 성명, 합격여부
        model.addAttribute("totalScore", totalScore); //점수
        model.addAttribute("testCount", testCount); //차시
        model.addAttribute("name", name); //사용자 성명
        model.addAttribute("completion", Test_status.합격); //합격여부에따른 이미지 표출용(비교대상)

        //[재시험보기] 표출시 필요한 쿼리파라미터
        model.addAttribute("studentCourseId", studentCourseId);;
        model.addAttribute("subCategoryId", subCategoryId);
        model.addAttribute("courseTitle", courseTitle);

        //3회 불합격으로 [다시 학습하기] 표출 시 필요한 쿼리파라미터
        model.addAttribute("applicationId", applicationId);

        //합격일시 별점 평가에 필요한 쿼리파라미터
        model.addAttribute("courseId", courseId);
        return "course/results-page";
    }




}
