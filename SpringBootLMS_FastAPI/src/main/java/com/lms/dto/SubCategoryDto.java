package com.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryDto {

    private long subCategoryId;
    private String subCategoryName;

    SubCategoryDto(long subCategoryId, String subCategoryName) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
    }

}
