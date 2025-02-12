package com.lms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long categoryId;

    /* 1차 카테고리 명  */
    @Column(nullable = false, length = 50, unique = true)
    private String categoryName;

    /* 1차 카테고리에 속하는 서브카테고리 리스트 */
    @OneToMany(mappedBy = "categories", cascade = CascadeType.ALL)
    private List<SubCategory> subCategories = new ArrayList<SubCategory>();

}
