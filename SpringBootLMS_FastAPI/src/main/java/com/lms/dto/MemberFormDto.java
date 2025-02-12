package com.lms.dto;

import com.lms.entity.CourseApplication;
import com.lms.entity.Member;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class MemberFormDto {

    private Long memberId;
    private String loginId;
    private String password;
    private String name;
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private String mobileNumber;
    private String address;
    private String email;

    private static ModelMapper modelMapper = new ModelMapper();
    /* Member -> MemberFormDto 변환 */
    public static MemberFormDto of(Member member) {
        return modelMapper.map(member, MemberFormDto.class);
    }



}
