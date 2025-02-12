package com.lms.service;

import com.lms.constant.Application_status;
import com.lms.constant.Completion_status;
import com.lms.constant.Enrollment_status;
import com.lms.constant.Test_status;
import com.lms.dto.*;
import com.lms.entity.*;
import com.lms.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class CourseService {

    /* 비디오 CRUD 구현 */
    private final VideoRepository videoRepository;

    /* 교육과정 CRUD 구현 */
    private final CourseRepository courseRepository;

    /* 교육영상 CRUD 구현 */
    private final CourseVideoRepository courseVideoRepository;

    /* 회원 CRUD 구현 */
    private final MemberRepository memberRepository;
    
    /* 교육신청내역 CRUD 구현 */
    private final CourseApplicationRepository courseApplicationRepository;

    /* 교육수강생관리 CRUD 구현 */
    private final StudentCourseRepository studentCourseRepository;

    /* 시험내역 CRUD 구현 */
    private final StudentTestRepository testEntityRepository;
    private final CourseHashTagRepository courseHashTagRepository;

    /* 영상 저장경로 */
    @Value("${courseVideoLocation}")
    private String courseVideoLocation;

    /* 썸네일 저장경로 */
    @Value("${courseImgLocation}")
    private String courseImgLocation;

    /* 파일업로드 / 삭제처리 객체생성 */
    private final FileService fileService;


    /* 영상 저장 로직 실행 START */
    public void saveVideo(VideoFormDto videoFormDto, MultipartFile videoFile) throws Exception{

        Videos videos = videoFormDto.createVideo();
        videoRepository.save(videos);

        /* Thumvivil.png */
        String oriVideoName = videoFile.getOriginalFilename();

        System.out.println("---------itemImgFile.getOriginalFilename()----------" + videoFile.getOriginalFilename());

        String videoName = "";
        String videoUrl = "";
        
        /* 이미지 파일이 업로드되었을 때,
        파일을 서버에 저장하고, 그 파일의 URL을 생성하여 반환하는 역할*/
        /* Thumvivil.png (oriImgName) 비어있지 않으면 */
        if(!StringUtils.isEmpty(oriVideoName)){

            /* 이미지 파일이 존재하면 fileService 업로드로직 실행 */
            /* FileService = uuid+확장자로 새로운 파일 명 생성,
               상품업로드 전체경로 생성 및 파일저장 c:/shop/item/62ecf0dc-9513-4b86-abf2-d1a506f70864.png
               새로운 파일 명 리턴 */

            videoName = fileService.uploadFile(courseVideoLocation, oriVideoName, videoFile.getBytes());
            /* [imgName] => 62ecf0dc-9513-4b86-abf2-d1a506f70864.png*/

            videoUrl = "/videos/video/" + videoName;
            /* [imgUrl] => /images/item/aa9148d3-e8c2-41a4-9e01-553a88daf5e9.png */

            /* 이 줄에서 save하면 첨부개수만큼만 저장됩니다. */
        }

        /* 엔티티 =>  이미지 정보 업데이트 메서드 호출 */
        /* itemImgLocation(파일 저장할 경로) => c:/shop/item */
        /* oriImgName(원본 파일명) => Thumvivil.png */
        /* itemImgFile.getBytes(업로드할 파일) => 69957 */
        videos.updateVideo(oriVideoName, videoName, videoUrl);
        videoRepository.save(videos);

    }
    /* 영상 저장 로직 실행 END  */


    /* 교육과정 저장 로직 실행 START */
    public void saveCourse(CourseFormDto courseFormDto, MultipartFile imgFile) throws Exception{

        /* CourseFormDto -> Courses 변환 */
        Courses courses = courseFormDto.createCourse();
        courseRepository.save(courses); // Courses 저장 (CascadeType.ALL에 의해 CourseVideo도 함께 저장됨)

        String oriImgName = imgFile.getOriginalFilename();
        System.out.println("---------imgFile.getOriginalFilename()----------" + imgFile.getOriginalFilename());
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(courseImgLocation, oriImgName, imgFile.getBytes());
            imgUrl = "/images/img/" + imgName;
        }

        courses.updateImg(oriImgName, imgName, imgUrl);
        courseRepository.save(courses);
       
        // 교육영상목록에 교육id가 null인 행에 현재 등록중인 교육객체 넣어주기
        List<CourseVideo> courseVideoList = courseVideoRepository.findByCoursesIsNull();
        for(CourseVideo courseVideo : courseVideoList){
            courseVideo.updatecourseId(courses);
        }
        List<CourseHashTag> courseHashTags = courseHashTagRepository.findByCourseIsNull();
        for(CourseHashTag courseHashTag : courseHashTags){
            courseHashTag.updatecourseId(courses);
        }

    }
    /* 교육과정 저장 로직 실행 END  */



    /* 상품 이미지 변경 START */

    /*public void updateCourseVideo(Long videoId, MultipartFile videoFile) throws Exception {

        *//* 기존 이미지는 지우고 새 이미지 저장*//*
        if(!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(videoId)
                    .orElseThrow( () -> new EntityNotFoundException(""));

            *//* 기존 이미지 파일 삭제 (itemImgFile 이 NULL 아니면실행 )*//*
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            *//* 새로 등록한 이미지로 변경 *//*
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            *//* savedItemImg = 영속상태, 데이터를 변경하는 것만으로 변경 감지 기능이 동작
               트랜잭션이 끝날 때 update쿼리가 실행됨. 여기서 중요한 것은
               엔티티가 영속 상태여야 함
            *//*

        }
    }*/
    /* 상품 이미지 변경 END */

   /* }*/

    // 사용자페이지 - 교육 전체 리스트 반환
    public Page<List<CourseListDto>> getCourseList(String keyword, Long categoryId, Long subCategoryId, Pageable pageable) {

        Page<List<CourseListDto>> userPage = courseRepository.findAllCourseWithCategoryInfo(keyword, categoryId, subCategoryId, pageable);
        return userPage;
    }

    // 사용자 페이지 - 특정교육 상세페이지 정보반환
    public CourseListDto CourseByCourseId(Long courseId){
        CourseListDto dto =  courseRepository.findCourseById(courseId);
        double totalRating = Math.round(dto.getTotalRating());
        dto.setTotalRating(totalRating);
        return dto;
    }


    // 사용자 페이지 - 특정교육 교육영상 반환
    public List<CourseVideoDto> findVideoById(Long courseId){
        List<CourseVideoDto> CourseVideoList = courseRepository.findVideoById(courseId);
        return CourseVideoList;
    }

    // 사용자 페이지 - 교육 신청시 신청내역 저장
    public Long saveApplication(Long courseId, String username){

        //username으로 멤버 객체 얻기
        Courses course = courseRepository.findById(courseId)
                .orElseThrow( ()-> new EntityNotFoundException() );
        course.setNumberOfApplications(course.getNumberOfApplications() + 1);
        courseRepository.save(course); //신청 수 카운트 +1

        Member member = memberRepository.findByLoginId(username);

        //신청내역 테이블 생성
        CourseApplication courseApplication = new CourseApplication();
        courseApplication.setCourse(course);
        courseApplication.setMember(member);
        courseApplication.setApplication_status(Application_status.신청완료);
        courseApplicationRepository.save(courseApplication); //DB에저장

        // 수강내역 테이블 생성
        StudentCourseDto studentCourseDto = new StudentCourseDto();
        studentCourseDto.setEnrollmentStatus(Enrollment_status.수강신청);
        studentCourseDto.setCompletionStatus(Completion_status.미수료);

        StudentCourse student = studentCourseDto.createStudentCourse();
        studentCourseRepository.save(student); //DB에저장

        //시험내역 테이블 생성
        StudentTestDto testEntityDto = new StudentTestDto();
        testEntityDto.setFirstAttemptStatus(Test_status.미응시);
        testEntityDto.setSecondAttemptStatus(Test_status.미응시);
        testEntityDto.setThirdAttemptStatus(Test_status.미응시);


        testEntityDto.setReset(false);


        //위에서 생성한 수강내역 테이블을 외래키로 지정
        StudentTest testEntity = testEntityDto.createTestEntity(testEntityDto);
        
        testEntity.setStudentCourse(student);
        testEntityRepository.save(testEntity); //DB에저장


        Long applicationId =  courseApplication.getApplicationId();
        CourseApplication application = courseApplicationRepository.findById(applicationId)
                .orElseThrow( ()-> new EntityNotFoundException() );
        student.setCourseApplication(application);

        return applicationId;
    }

    /* 관리자 페이지 교육과정 리스트 조회 */
    public List<AdminCourseListDto> getAdminCourseList(){
        List<AdminCourseListDto> list = courseRepository.findAllAdminCourseList();

        //각 리스트별 수료율 구하기
        for (AdminCourseListDto dto : list) {
            List<StudentCourse> studentList = new ArrayList<>(); /* 교육별 수강생을 저장하기 위한 리스트 */
            Long courseId = dto.getCourseId(); //교육과정별 교육ID
         
            //교육ID에 해당하는 교육신청내역 => 교육신청내역에 해당하는 수강생 목록 조회
            List<CourseApplication> app =
                    courseApplicationRepository.findByCourse_CourseId(courseId);
            for (CourseApplication application : app) {
                Long appId = application.getApplicationId();
                StudentCourse student =  studentCourseRepository.findByCourseApplication_ApplicationId(appId);
                studentList.add(student); /* 한 교육과정의 수강생 리스트 저장 */
            }
            dto.setCompletionRate(getCompletionRate(studentList));
            //수강생 리스트로 수료율 구하는 함수 실행후 수료율 리턴받기
        }
        return list;
    }

    
    //수강생 목록으로 수료율 구하는 메서드
    public double getCompletionRate(List<StudentCourse> studentList){

        int totalStudent = studentList.size(); //총 수강생 수
        int completedStudent = 0; //해당 교육 수강생의 수료 수
        log.info("totalStudent:" + totalStudent + " /  completedStudent:" + completedStudent);

        for(StudentCourse student : studentList){
            if(student.getCompletionStatus() == Completion_status.수료) {
                completedStudent++;
            }
        }
        
        if (totalStudent == 0) {
            return 0.0; // 예시로 0.0을 반환
        }
        /* (수료자수 / 총 수강생 수) *100하기 */
        return((double) (completedStudent /totalStudent)*100);
    }

    //메인비주얼 교육 최근등록순 5개 + 해당 교육과정의 해시태그 리스트 가져오기
    public List<CourseListDto> getMainVisualList(){
        return getHashTag(courseRepository.getMainVisualList());
    }

    //가장 많이 신청한 교육과정 8개
    public List<CourseListDto> top8CourseList(){
        return  getHashTag(courseRepository.top8CourseList());
    }


    //교육과정 리스트 전달받아 해당 교육과정의 해시태그 리스트 추가하여 리턴해주는 메서드
    private  List<CourseListDto> getHashTag(List<CourseListDto> dtos){

        for (CourseListDto dto : dtos) {  //교육과정별 courseId구해 해당 Id의 해시태그 조회하기

            List<HashTagFormDto> hashTagFormDtos = new ArrayList<>();
            double totalRating = Math.round(dto.getTotalRating());
            dto.setTotalRating(totalRating);

            long courseId = dto.getCourseId();

            //courseId에 해당하는 해시태그 엔티티 리스트
            List<CourseHashTag> courseHashTags =
                    courseHashTagRepository.findByCourse_CourseId(courseId);

            //해시태그 엔티티 Dto로 변환후 해시태그 리스트에 넣기
            for (CourseHashTag courseHashTag : courseHashTags) {
                HashTagFormDto hashTagFormDto = HashTagFormDto.of(courseHashTag);
                hashTagFormDtos.add(hashTagFormDto);
            }
            //해시태그 리스트를 교육과정 dto에 넣기
            dto.setHashTagFormDtoList(hashTagFormDtos);
        }
        return dtos;
    }

    //사용자가 입력한 별점 / 교육ID 받아 새로운 평균 업데이트하기
    public void saveRating(int rating, long courseId){
        Courses course = courseRepository.findById(courseId).orElseThrow( ()-> new EntityNotFoundException() );

        int currentRatingCount = course.getRatingCount(); //현재까지 평가자 수
        double currentTotalRating = course.getTotalRating(); //현재까지 평균별점

        int newRatingCount = currentRatingCount + 1;
        double newTotalRating = currentTotalRating + rating;

        //새 평균 계산
        double newAverageRating = (double) newTotalRating / newRatingCount;
        System.out.println("newAverageRating => " + newAverageRating);

        course.updateRating(newAverageRating, newRatingCount);
        courseRepository.save(course);
    }


    public List<CourseListDto> getRecommendCoursesData(List<Integer> courseIds) {

        List<Long> longCourseIds = courseIds.stream()
                .map(Integer::longValue) // Integer를 Long으로 변환
                .collect(Collectors.toList()); // 리스트로 수집

        List<CourseListDto> dtos = new ArrayList<>();

        for(int i=0; i<longCourseIds.size(); i++){
            dtos.add( courseRepository.findAllByInCourseId(longCourseIds.get(i)));
        }
        return dtos;
    }
}
