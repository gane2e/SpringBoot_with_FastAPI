package com.lms.dto;

import com.lms.entity.Videos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class VideoFormDto {

    /* 교육영상 DTO */
    private Long videoId;

    @NotBlank(message = "영상 제목은 필수 입력값입니다.")
    private String title; /* 영상 제목 */

    private String videoName; /* 영상 파일명 */

    private String oriVideoName; /* 영상파일 원본 이름 */

    private String videoUrl; /* 영상파일 조회 경로 */

    @NotNull(message = "서브 카테고리 값이 비어있습니다.")
    private Long subCategoryId; //해당 영상의 카테고리id

    private static ModelMapper modelMapper = new ModelMapper();

    public Videos createVideo() {
        return modelMapper.map(this, Videos.class);
    }

    /* videos -> VideoFormDto 변환 */
    public static VideoFormDto of(Videos videos) {
        return modelMapper.map(videos, VideoFormDto.class);
    }
}
