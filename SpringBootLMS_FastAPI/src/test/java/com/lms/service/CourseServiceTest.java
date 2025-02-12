package com.lms.service;

import com.lms.dto.CourseFormDto;
import com.lms.dto.CourseListDto;
import com.lms.entity.Courses;
import groovy.util.logging.Log4j2;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class CourseServiceTest {

    @Autowired
    CourseService courseService;

   /* @Test
    public void findAll() {
        List<CourseListDto> courseList = courseService.getCourseList();
        courseList.forEach(course ->
                System.out.println("CourseTitle => " + course.getTitle())
        );
    }*/

}