package com.surveine.service;

import com.surveine.config.SecurityUtil;
import com.surveine.domain.Abox;
import com.surveine.domain.Cbox;
import com.surveine.domain.Member;
import com.surveine.dto.abox.AboxDTO;
import com.surveine.dto.cbox.CboxDTO;
import com.surveine.dto.member.MemberPageRspDTO;
import com.surveine.dto.member.MemberRegisterRspDTO;
import com.surveine.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AnsRepository ansRepository;
    private final EnqRepository enqRepository;
    private final CboxRepository cboxRepository;
    private final AboxRepository aboxRepository;


    public MemberRegisterRspDTO getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberRegisterRspDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    public String getNameById(Long memberId) {
        return memberRepository.getOne(memberId).getName();
    }

    public Member getOneMemberById(Long memberId) {
        return memberRepository.getOne(memberId);
    }

    public Map<String, Object> getMemberProfile() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Map<String, Object> memberPageMap = new HashMap<>();
        Optional<Member> nowMember = memberRepository.findById(memberId);

        MemberPageRspDTO rspDTO = MemberPageRspDTO.builder()
                .email(nowMember.get().getEmail())
                .gender(nowMember.get().getGender())
                .name(nowMember.get().getName())
                .birthday(nowMember.get().getBirthday())
                .build();


        memberPageMap.put("member", rspDTO);
        memberPageMap.put("enqCount", enqRepository.countByMemberId(memberId));
        memberPageMap.put("ansCount", ansRepository.countByMemberId(memberId));

        return memberPageMap;
    }
}
