package com.surveine.dto.member;

import com.surveine.enums.Authority;
import com.surveine.enums.GenderType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class MemberPageRspDTO {
    private String email;
    private String name;
    private String birthday;
    private GenderType gender;

    @Builder
    public MemberPageRspDTO(String email, String name, String birthday, GenderType gender) {
        this.email = email;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }
}
