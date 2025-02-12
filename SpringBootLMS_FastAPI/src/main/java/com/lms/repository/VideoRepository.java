package com.lms.repository;

import com.lms.dto.VideoListDto;
import com.lms.entity.Category;
import com.lms.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Videos, Long> {

    // 관리자 > 교육등록 > 카테고리선택 > 선택한 카테고리의 교육영상 등록일기준 내림차순 찾기
    List<Videos> findBySubCategory_SubCategoryId(Long subCategoryId);

    @Query("SELECT new com.lms.dto.VideoListDto(c.categoryName, s.subCategoryName, v.title, v.oriVideoName, " +
            "v.videoId, v.regTime, v.updateTime, v.createdBy, v.modifiedBy) " +
            "FROM Videos v " +
            "JOIN v.subCategory s " +
            "JOIN s.categories c " +
            "order by v.regTime desc")
    List<VideoListDto> findVideoWithCategoryInfo();

}
