package com.lms.repository;

import com.lms.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {

    List<Questions> findBySubCategory_SubCategoryId(Long subCategoryId);


}
