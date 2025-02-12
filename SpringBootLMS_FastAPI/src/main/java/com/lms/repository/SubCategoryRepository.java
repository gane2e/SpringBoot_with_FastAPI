package com.lms.repository;

import com.lms.dto.SubCategoryDto;
import com.lms.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    @Query("SELECT new com.lms.dto.SubCategoryDto(s.subCategoryId, s.subCategoryName) " +
            "FROM SubCategory s " +
            "WHERE s.categories.categoryId = :categoryId")
    List<SubCategoryDto> findSubCategoriesByCategoryId(@Param("categoryId") Long categoryId);
}
