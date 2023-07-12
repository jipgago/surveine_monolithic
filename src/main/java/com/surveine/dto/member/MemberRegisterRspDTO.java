package com.surveine.dto.member;

import com.surveine.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegisterRspDTO {

    private Long id;
    private String email;

    @Builder
    public MemberRegisterRspDTO(Member member) {
        this.id = member.getId();
        this.email = getEmail();
    }

    public static MemberRegisterRspDTO of(Member member) {
        return MemberRegisterRspDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .build();
    }
}
