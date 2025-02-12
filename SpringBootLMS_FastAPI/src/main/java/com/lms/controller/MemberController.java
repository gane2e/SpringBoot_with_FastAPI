package com.lms.controller;

import com.lms.constant.Completion_status;
import com.lms.constant.Enrollment_status;
import com.lms.dto.DashBoardCountDto;
import com.lms.dto.MemberFormDto;
import com.lms.dto.StudentCourseHisDto;
import com.lms.entity.Member;
import com.lms.repository.MemberRepository;
import com.lms.service.EmailService;
import com.lms.service.MemberService;
import com.lms.service.StudentCourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/members/")
public class MemberController {


    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final StudentCourseService studentCourseService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public MemberController(MemberService memberService, MemberRepository memberRepository, StudentCourseService studentCourseService) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.studentCourseService = studentCourseService;
    }

    @GetMapping(value = "/login")
    public String memberLogin(Model model) {
        log.info("login");
        model.addAttribute("pageTitle", "로그인");
        return "member/memberLoginForm";
    }

    //로그인실패시 Get요청으로 실패메시지 전달
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주십시오.");
        model.addAttribute("pageTitle", "로그인");
        return "member/memberLoginForm";
    }


    @GetMapping(value = "/register")
    public String memberRegister(Model model) {
        model.addAttribute("pageTitle", "회원가입");
        model.addAttribute("member", new MemberFormDto());
        return "member/register";
    }

    @PostMapping(value = "/checkloginId")
    public ResponseEntity<String> checkloginId(@RequestBody MemberFormDto memberFormDto) {
        String loginId = memberFormDto.getLoginId();
        boolean result =
                memberService.checkloginId(loginId);
        if(result) {
            return ResponseEntity.ok().body("true");
        } else {
            return ResponseEntity.ok().body("false");
        }
    }



    @PostMapping(value = "/new")
    public String memberRegister(MemberFormDto memberFormDto, Model model) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Member member = Member.createMember(memberFormDto,passwordEncoder);
        memberService.saveMember(member);
        model.addAttribute("success", "회원가입이 완료되었습니다.");
        return "redirect:/";
    }

    @GetMapping(value = "/modify")
    public String memberModify(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MemberFormDto memberFormDto =  memberService.modifyMember(username);
        model.addAttribute("member",memberFormDto);
        model.addAttribute("pageTitle", "회원정보 수정");

        return "member/memberFormModify";
    }

    /* 마이페이지 수강현황 대시보드 */
    @GetMapping(value = "/dashBoard")
    public String dashBoard(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberRepository.findByLoginId(username);
        Long memberId = member.getId();
        String userName = member.getName();
        Date birthDate = member.getBirthdate();

        List<StudentCourseHisDto> dtos =
                studentCourseService.findStudentCourseHis(memberId);

        List<StudentCourseHisDto> hisDtos
                = studentCourseService.updateTestHistory(dtos);


        DashBoardCountDto dashBoardCountDto = new DashBoardCountDto();
        int enrollment_enrollment = 0; //수강신청 상태 수
        int enrollment_progress = 0; //학습중 수
        int enrollment_completed = 0; //학습완료 수
        int completion_completed = 0; //수료 수
        int completion_notCompleted = 0; // 미수료
        int total_enrollment = 0; //총 신청과정 수
        for (StudentCourseHisDto dto : hisDtos) {
            if (dto.getEnrollmentStatus() == Enrollment_status.수강신청) {
                enrollment_enrollment++;
            } else if (dto.getEnrollmentStatus() == Enrollment_status.학습중) {
                enrollment_progress++;
            } else if (dto.getEnrollmentStatus() == Enrollment_status.학습완료) {
                enrollment_completed++;
            }
            if (dto.getCompletionStatus() == Completion_status.수료) {
                completion_completed++;
            } else if (dto.getCompletionStatus() == Completion_status.미수료) {
                completion_notCompleted++;
            }
        } //FOR END
        total_enrollment = completion_completed + completion_notCompleted;
        dashBoardCountDto.setEnrollment_enrollment(enrollment_enrollment);
        dashBoardCountDto.setEnrollment_progress(enrollment_progress);
        dashBoardCountDto.setEnrollment_completed(enrollment_completed);
        dashBoardCountDto.setCompletion_completed(completion_completed);
        dashBoardCountDto.setCompletion_notCompleted(completion_notCompleted);
        dashBoardCountDto.setTotal_enrollment(total_enrollment);

        model.addAttribute("pageTitle", "나의 수강현황");
        model.addAttribute("hisDtos", hisDtos);
        model.addAttribute("dashBoardCount", dashBoardCountDto);
        model.addAttribute("userName", userName); //유저 성명
        model.addAttribute("birthDate", birthDate); //유저 생년월일
        model.addAttribute("enrollmentStatus", Enrollment_status.학습완료);
        model.addAttribute("completionStatus", Completion_status.수료);
        return "member/dashBoard";
    }

    @PostMapping(value = "/email-find-check")
    public ResponseEntity<String> emailCheck(@RequestBody MemberFormDto memberDto) {

        boolean check = memberService.emailCheck(
                memberDto.getEmail()
        );
        if(check){ //해당이메일로 가입된 회원이 있으면 이메일을 뷰로 다시전송
            return ResponseEntity.ok().body("true");
        } else {
            return ResponseEntity.ok().body("false");
        }
    }
    
    //비밀번호 찾기 시 아이디 인증 메서드
    @PostMapping(value = "/id-find-check")
    public ResponseEntity<HashMap<String, String>> idCheck(@RequestBody MemberFormDto memberDto) {

        boolean check = memberService.loginIdCheck(
                memberDto.getLoginId()
        );
        HashMap<String, String> response = new HashMap<>();
        if(check){ //아이디로 가입된 회원이 있으면 이메일을 뷰로 다시전송
            Member member = memberRepository.findByLoginId( memberDto.getLoginId());
            response.put("email", member.getEmail());
        }
        return ResponseEntity.ok(response);
    }


    /* 인증번호 요청받기 */
    @PostMapping(value = "/idPw-find")
    public ResponseEntity<HashMap<Object, Object>> ifFind(@RequestBody MemberFormDto memberDto) {

        String email = memberDto.getEmail();
        String subject = "아이디/비밀번호 찾기 인증번호가 도착했습니다.";
        String templateName = "Mail-id-find";
        int certificationNumber = generateAuthNo4(); //인증번호 6자리 생성

        emailService.sendEmailIdFind(email, subject, templateName, certificationNumber);

        HashMap<Object, Object> response = new HashMap<>();
        response.put("certificationNumber", certificationNumber);

        Member member = memberRepository.findByEmail(email);
        response.put("loginId", member.getLoginId());

        System.out.println("certificationNumber = " + certificationNumber);
        return ResponseEntity.ok(response);
    }


    public static int generateAuthNo4() {
        return (int)(Math.random() * 899999) + 100000;
    }


    @PostMapping(value = "/modifyPass")
    public void modifyPassword(@RequestBody MemberFormDto memberFormDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        Member member = memberRepository.findByLoginId(loginId);
        member.updatePassword(passwordEncoder , memberFormDto.getPassword());
        memberRepository.save(member);
    }

}

