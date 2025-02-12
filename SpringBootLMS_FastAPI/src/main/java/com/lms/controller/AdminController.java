package com.lms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.dto.*;
import com.lms.entity.Category;
import com.lms.service.*;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/admin/")
public class AdminController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private CourseVideoService courseVideoService;
    @Autowired
    private CourseHashTagService courseHashTagService;

    // 교육과정 목록
    @GetMapping(value = "/courseList")
    public String courseList(Model model) {

        List<AdminCourseListDto> adminCourseListDtos = courseService.getAdminCourseList();
        model.addAttribute("courseList", adminCourseListDtos);

        return "admin/course/courseList";
    }

    // 관리자 영상 목록
    @GetMapping(value = "/videoList")
    public String videoList(Model model) {

        List<VideoListDto> videoList  =videoService.findAllVideosByCategoryInfo();
        model.addAttribute("videoList", videoList);

        for (VideoListDto video : videoList) {
            System.out.println(video.getTitle());
            System.out.println("--------------------");
        }

        return "admin/video/videoList";
    }

    // 시험문제 목록
    @GetMapping(value = "/questionList")
    public String questionList(Model model) {

        return "admin/question/questionList";
    }


    // 시험문제 등록페이지
    @GetMapping(value = "/newQuestion")
    public String newQuestion(Model model) {

        return "admin/question/newQuestion";
    }



    /* 교육과정 등록페이지 */
    @GetMapping(value = "/newCourse")
    public String newCourse(Model model) {

        // 1차카테고리 얻어서 모델로 보내기
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("CourseFormDto", new CourseFormDto());

        return "admin/newCourse";
    }


    /* 영상 등록페이지 */
    @GetMapping(value = "/newVideo")
    public String newVideo(Model model) {

        // 1차카테고리 얻어서 모델로 보내기
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("videoFormDto", new VideoFormDto());

        return "admin/newVideo";
    }


    /* 영상 등록 요청 */
    @PostMapping(value = "/newVideo")
    public String newVideoReg(@Valid VideoFormDto videoFormDto, BindingResult bindingResult,
                              Model model, @RequestParam("videoFile") MultipartFile videoFile ) {

        System.out.println("영상 등록 요청");
        
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult : 에러발생");
            return "error/404";
        }
        if (videoFile.isEmpty()) {
            System.out.println("videoFile.isEmpty() : 필수");
            model.addAttribute("errorMsg", "영상은 필수 첨부입니다.");
        }
        try {
            courseService.saveVideo(videoFormDto, videoFile);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "영상 등록중 에러가 발생하였습니다.");
            System.out.println("catch : 에러발생");
            System.out.println(e.getMessage());
            return "error/404";
        }

        log.info("영상등록 성공!");

        return "redirect:/admin/videoList";
    }



    /*// 관리자 영상 상세페이지
    @GetMapping(value = "/video/${videoId}")
    public String videoDtl(@PathVariable("videoId") Long videoId, Model model) {

      *//*  // 비디오 정보 반환
        List<VideoListDto> videoList  =videoService.findAllVideosByCategoryInfo();
        model.addAttribute("videoList", videoList);

        for (VideoListDto video : videoList) {
            System.out.println(video.getTitle());
            System.out.println("--------------------");
        }*//*

        return "admin/video/videoDtl";
    }*/


    // 교육과정 등록 시 선택한 카테고리의 영상목록 반환하기
    @GetMapping("/getVideosBySubCategory")
    @ResponseBody
    public List<VideoFormDto> getVideosBySubCategory(@RequestParam("subCategoryId") Long subCategoryId
            , Model model) {

        // 위에서 선택한 카테고리별 교육영상 불러오기
        List<VideoFormDto> scByVideoList = videoService.findVideosBySubCategoryId(subCategoryId);

        model.addAttribute("videoList", scByVideoList);

        for (VideoFormDto video : scByVideoList) {
            System.out.println("video ID : " + video.getVideoId());
            System.out.println("video Title : " + video.getTitle());
            System.out.println("video fileName : " + video.getOriVideoName());
            System.out.println("--------------------");
        }

        return scByVideoList;
    }


    //교육과정 - 영상요청 테스트
    @PostMapping(value = "/newCourse/addData")
    public ResponseEntity<String> createNewCourse(@RequestBody Map<String, Object> formData) {

        Map<String, Map<String, String>> selectedVideoData = (Map<String, Map<String, String>>) formData.get("selectedVideoData");
        List<String> hashTagFormDtoList = (List<String>) formData.get("hashTagFormDtoList");

        System.out.println("selectedVideoData: " + selectedVideoData);
        System.out.println("hashTagFormDtoList: " + hashTagFormDtoList);

        // HashTagFormDto 객체 리스트 생성
        List<HashTagFormDto> hashTags = new ArrayList<>();

        // 해시태그 리스트를 순회하여 HashTagFormDto 객체에 setHashTagName을 사용해 저장
        if (hashTagFormDtoList != null) {
            for (String hashTagName : hashTagFormDtoList) {
                HashTagFormDto hashTagFormDto = new HashTagFormDto();
                hashTagFormDto.setHashTagName(hashTagName);  // 해시태그 값을 set
                hashTags.add(hashTagFormDto);  // 리스트에 추가
            }
        } else
            System.out.println("hashTagFormDtoList is null");
        
        //해시태그 먼저 저장
        courseHashTagService.saveHashTag(hashTags);

        selectedVideoData.forEach((key, value) -> {

            String videoIdString = value.get("videoId");
            String rowCountString = value.get("rowCount");
            Long videoId = null;
            Long rowCount = null;

            try {
                videoId = Long.parseLong(videoIdString); // 또는 Long.valueOf(videoIdString)
                rowCount = Long.parseLong(rowCountString); // 또는 Long.valueOf(rowCountString)
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + e.getMessage());
            }

            // CourseVideoDto 객체 생성 및 값 설정
            CourseVideoDto courseVideoDto = new CourseVideoDto();
            courseVideoDto.setVideoId(videoId);
            courseVideoDto.setCourseVideoIndex(rowCount);

            System.out.println("------------------------------------------");
            System.out.println("Key: " + key);
            System.out.println("VideoId: " + videoId);
            System.out.println("RowCount: " + rowCount);

            courseVideoService.saveCourseVideo(courseVideoDto);
        });
        return ResponseEntity.ok("Course Created Successfully");
    }


    // 교육과정 등록 요청
    @PostMapping(value = "/newCourse")
    public String newCourse(@Valid CourseFormDto courseFormDto, BindingResult bindingResult,
                            Model model,
                            @RequestParam("imgFile") MultipartFile imgFile) {

        System.out.println("교육과정 등록 요청");
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult : 에러발생");

            bindingResult.getAllErrors().forEach(error -> {
                log.warn("유효성 검사 에러 발생: {} - {}", error.getDefaultMessage(), error.getObjectName() + "." + error.getCode());
            });
            return "error/404";
        }

        if (imgFile.isEmpty()) {
            System.out.println("imgFile.isEmpty() : 필수");
            model.addAttribute("errorMsg", "썸네일은 필수 첨부입니다.");
        }

        try {
            courseService.saveCourse(courseFormDto, imgFile);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "교육과정 등록중 에러가 발생하였습니다.");
            System.out.println("catch : 에러발생");
            System.out.println(e.getMessage());
            return "error/404";
        }

        log.info("교육과정 등록 성공!");

        return "redirect:/admin/newCourse";

    }




}
