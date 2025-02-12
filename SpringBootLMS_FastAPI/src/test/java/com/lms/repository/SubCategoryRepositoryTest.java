package com.lms.repository;

import com.lms.entity.Category;
import com.lms.entity.SubCategory;
import groovy.util.logging.Log4j2;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class SubCategoryRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(SubCategoryRepositoryTest.class);
    @Autowired
    private EntityManager entityManager; // 테스트용 EntityManager

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    void findSubCategoriesByCategoryIdTest() {
        // Given (테스트 데이터 준비)
        Category category = new Category();
        category.setCategoryName("테스트 카테고리2");
        entityManager.persist(category); // 영속화

        SubCategory subCategory1 = new SubCategory();
        subCategory1.setSubCategoryName("테스트 서브 카테고리2-1");
        subCategory1.setCategories(category); // 연관 관계 설정
        entityManager.persist(subCategory1);

        SubCategory subCategory2 = new SubCategory();
        subCategory2.setSubCategoryName("테스트 서브 카테고리2-2");
        subCategory2.setCategories(category); // 연관 관계 설정
        entityManager.persist(subCategory2);

        entityManager.flush(); // 영속성 컨텍스트 동기화
        entityManager.clear(); // 영속성 컨텍스트 초기화

        // When (테스트 실행)
       // List<SubCategory> subCategories = subCategoryRepository.findSubCategoriesByCategoryId(category.getCategoryId());


    }

    @Test
    void findSubCategoriesByCategoryId_NoSubcategoriesTest() {
        /*// Given (테스트 데이터 준비)
        Category category = new Category();
        category.setCategoryName("테스트 카테고리");
        entityManager.persist(category);

        entityManager.flush();
        entityManager.clear();*/

        // When
        //List<SubCategory> subCategories = subCategoryRepository.findSubCategoriesByCategoryId(102L);

    }

}