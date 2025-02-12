package com.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sub_categories")
@Getter
@Setter
public class SubCategory {

    @Id
    @Column(name = "sub_category_id")
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long subCategoryId;

    /* 2차 카테고리 명  */
    @Column(nullable = false, length = 50, unique = true, name = "sub_category_name")
    private String subCategoryName;

    @ManyToOne(fetch = FetchType.LAZY) /* 1차 카테고리 테이블 조인 */
    @JoinColumn(name = "category_id")
/*    @JsonIgnore // 순환 참조 방지 (필요한 경우), 양방향 참조시 무한루프 발생가능*/
    private Category categories;



}
