package com.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashBoardCountDto {

    private int enrollment_enrollment; //수강신청 수
    private int enrollment_progress; //학습중 수
    private int enrollment_completed;  //학습완료 수
    private int completion_completed; //수료 수
    private int completion_notCompleted; //미수료 수
    private int total_enrollment; //총 신청과정 수
}
