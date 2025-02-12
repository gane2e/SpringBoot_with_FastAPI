package com.lms.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.dto.SubCategoryDto;
import com.lms.entity.Category;
import com.lms.entity.SubCategory;
import com.lms.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("/categories")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories"; // categories.html
    }

    @GetMapping("/subcategories")
    @ResponseBody
    public List<SubCategoryDto> getSubCategories(@RequestParam("categoryId") Long categoryId) {
        List<SubCategoryDto> subCategoryList =  categoryService.getSubCategoriesByCategoryId(categoryId);

        // 순환 참조가 발생할 수 있는 부분을 직렬화 전에 로그로 찍어봄
        try {
            // subCategoryList의 JSON 직렬화
            String subCategoryListJson = objectMapper.writeValueAsString(subCategoryList);
            log.info("subCategoryList JSON: {}", subCategoryListJson);
        } catch (Exception e) {
            log.error("직렬화 중 오류 발생: ", e);
        }

        return subCategoryList;
    }



}
