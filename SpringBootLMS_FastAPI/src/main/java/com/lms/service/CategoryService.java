package com.lms.service;

import com.lms.dto.SubCategoryDto;
import com.lms.entity.Category;
import com.lms.entity.SubCategory;
import com.lms.repository.CategoryRepository;
import com.lms.repository.SubCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId) {
        List<SubCategoryDto> subCategoryList = subCategoryRepository.findSubCategoriesByCategoryId(categoryId);
        return subCategoryList;
    }
}
