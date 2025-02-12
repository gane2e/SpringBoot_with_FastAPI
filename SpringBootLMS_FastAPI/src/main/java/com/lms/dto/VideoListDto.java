package com.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class VideoListDto {

    /* 교육영상 DTO */
    private String categoryName;
    private String subCategoryName;
    private String title;
    private String oriVideoName; /* 영상파일 원본 이름 */
    private Long videoId;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private String createdBy;
    private String modifiedBy;

    // 생성자
    public VideoListDto(String categoryName, String subCategoryName, String title, String oriVideoName,
                        Long videoId, LocalDateTime regTime, LocalDateTime updateTime,
                        String createdBy, String modifiedBy) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.title = title;
        this.oriVideoName = oriVideoName;
        this.videoId = videoId;
        this.regTime = regTime;
        this.updateTime = updateTime;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }


}
