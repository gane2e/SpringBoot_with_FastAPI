package com.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HashTagCountDto {
    private long count;
    private String hashTagName;

    HashTagCountDto(long count, String hashTagName) {
        this.count = count;
        this.hashTagName = hashTagName;
    }
}
