package com.surveine.dto.member;

import lombok.Getter;

@Getter
public class MemberRegisterEmailRspDTO {
    private String token;

    public MemberRegisterEmailRspDTO(String token){
        this.token = token;
    }
}
