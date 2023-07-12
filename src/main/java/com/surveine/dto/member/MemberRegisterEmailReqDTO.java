package com.surveine.dto.member;

import lombok.Getter;

@Getter
public class MemberRegisterEmailReqDTO {
    private final String email;

    public MemberRegisterEmailReqDTO(String email){
        this.email = email;
    }
}
