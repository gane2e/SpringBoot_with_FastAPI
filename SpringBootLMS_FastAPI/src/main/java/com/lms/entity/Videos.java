package com.lms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "videos")
@Getter
@Setter
public class Videos extends BaseEntity {

    @Id
    @Column(name = "video_id")
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long videoId;

    // 영상제목
    @Column(nullable = false, length = 50)
    private String title;

    private String videoName; /* 영상 파일명 */

    private String oriVideoName; /* 영상파일 원본 이름 */

    private String videoUrl; /* 영상파일 조회 경로 */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    /* 원본명, 업데이트 이미지명, 이미지 경로를 파라미터로 입력받아 이미지 정보를 업데이트하는 메서드 */
    public void updateVideo(String oriVideoName, String videoName, String videoUrl){
        this.oriVideoName = oriVideoName;
        this.videoName = videoName;
        this.videoUrl = videoUrl;
    }

}
