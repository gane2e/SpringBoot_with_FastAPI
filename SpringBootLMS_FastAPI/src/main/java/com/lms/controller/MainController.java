package com.lms.controller;

import com.lms.dto.CourseListDto;
import com.lms.dto.HashTagCountDto;
import com.lms.dto.HashTagFormDto;
import com.lms.entity.Courses;
import com.lms.service.CourseHashTagService;
import com.lms.service.CourseService;
import com.lms.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/")
public class MainController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseHashTagService courseHashTagService;

    @Autowired
    private MemberService memberService;

    @GetMapping(value = "/")
    public String index(Model model) {

        //메인비주얼 교육목록 (최근등록순 5개)
        List<CourseListDto> getMainVisualList = courseService.getMainVisualList();
        model.addAttribute("mainVisualList", getMainVisualList);
        
        //메인검색기 해시태그 표출(가장 많이등록된순 5개)
        List<HashTagCountDto> top5Hashtags = courseHashTagService.findByTop5Hashtags();
        model.addAttribute("top5Hashtags", top5Hashtags);

        //가장 많이 신청한 교육과정 8개
        List<CourseListDto> top8CourseList = courseService.top8CourseList();
        model.addAttribute("top8CourseList", top8CourseList);
        return "index";
    }

    @GetMapping(value = "/success")
    public String loginSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "로그인 되었습니다.");
        return "redirect:/";
    }

    //로그인 여부 체크하는 공통 ajax 처리
    @GetMapping(value = "/loginCheck")
    public ResponseEntity<String> loginCheck() {

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("authentication : " + authentication.getName());
            return ResponseEntity.ok("로그인 상태입니다."); // 로그인 상태
        } else {
            return ResponseEntity.status(401).body("로그인되지 않았습니다."); // 비로그인 상태
        }
    }

    //유저 기본키 체크하는 메서드
    @PostMapping(value = "/api/userId")
    public ResponseEntity<Map<String, Object>>userId(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> map = new HashMap<>();
        String username = userDetails.getUsername();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = memberService.userId(username);
            String name = memberService.userName(authentication.getName());
            map.put("username", name);
            map.put("userId", userId);
        } catch (Exception e) {
            // 로그를 통해 문제를 확인
            e.printStackTrace();
            // 적절한 에러 응답 반환
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user ID", e);
        }
        return ResponseEntity.ok(map);
    }



    @GetMapping(value = "/id-find")
    public String idFind(Model model) {
        model.addAttribute("pageTitle", "아이디/비밀번호 찾기");
        return "member/id-find";
    }

    @GetMapping(value = "/pw-find")
    public String pwFind(Model model) {
        model.addAttribute("pageTitle", "아이디/비밀번호 찾기");
        return "member/pw-find";
    }


}
