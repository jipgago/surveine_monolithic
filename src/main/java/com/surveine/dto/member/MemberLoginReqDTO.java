package com.surveine.dto.member;

import com.surveine.enums.Authority;
import com.surveine.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginReqDTO {
    private String email;
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
