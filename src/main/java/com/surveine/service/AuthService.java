package com.surveine.service;

import com.surveine.domain.*;
import com.surveine.dto.member.*;
//import com.surveine.dto.member.MemberRegisterReqDTO;
//import com.surveine.dto.member.TokenDTO;
import com.surveine.enums.AnsStatus;
import com.surveine.enums.Authority;
import com.surveine.enums.DistType;
import com.surveine.enums.EnqStatus;
import com.surveine.repository.FavRepository;
import com.surveine.repository.MemberRepository;
import com.surveine.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CboxService cboxService;
    private final AboxService aboxService;
    private final AnsService ansService;
    private final EnqService enqService;
    private final FavRepository favRepository;

    @Transactional
    public void signup(MemberRegisterReqDTO memberRegisterReqDTO) {
        if (memberRepository.existsByEmail(memberRegisterReqDTO.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member newMember = Member.builder()
                .email(memberRegisterReqDTO.getEmail())
                .name(memberRegisterReqDTO.getName())
                .password(passwordEncoder.encode(memberRegisterReqDTO.getPassword()))
                .birthday(memberRegisterReqDTO.getBirthday())
                .gender(memberRegisterReqDTO.getGender())
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(newMember);
        Cbox cbox = Cbox.builder()
                    .member(newMember)
                    .name("기본 제작함")
                    .build();
        cboxService.create(cbox);

        Cbox cbox1 = Cbox.builder().member(newMember).name("차세대챗봇").build();
        Cbox cbox2 = Cbox.builder().member(newMember).name("데이터관리기술").build();
        Cbox cbox3 = Cbox.builder().member(newMember).name("시스템아키텍처").build();
        cboxService.create(cbox1);
        cboxService.create(cbox2);
        cboxService.create(cbox3);

//            Enq enq = Enq.builder()
//                    .member(newMember)
//                    .cbox(cbox)
//                    .name("기본 설문지 이름")
//                    .title("기본 설문지 제목")
//                    .isShared(false)
//                    .enqStatus(EnqStatus.ENQ_MAKE)
//                    .distType(DistType.LINK)
//                    .updateDate(null)
//                    .build();
//            enqService.create(enq);
        Abox abox = Abox.builder()
                .member(newMember)
                .name("기본 참여함")
                .build();
        aboxService.create(abox);

        Enq enq = Enq.builder()
                .member(newMember)
                .cbox(cbox)
                .name("삭제금지 ans테스트용")
                .title("임시")
                .isShared(false)
                .enqStatus(EnqStatus.DIST_DONE)
                .distType(DistType.LINK)
                .updateDate(LocalDate.now())
                .build();
        enqService.create(enq);

        Enq enqSBoxTest1 = Enq.builder()
                .member(newMember)
                .cbox(cbox)
                .name("좋아요하나있는설문")
                .title("임시")
                .isShared(true)
                .enqStatus(EnqStatus.DIST_DONE)
                .distType(DistType.LINK)
                .favCount(4L)
                .updateDate(LocalDate.now())
                .build();

        Enq enqSBoxTest2 = Enq.builder()
                .member(newMember)
                .cbox(cbox)
                .name("좋아요없는설문")
                .title("임시")
                .isShared(true)
                .enqStatus(EnqStatus.DIST_DONE)
                .distType(DistType.LINK)
                .favCount(0L)
                .updateDate(LocalDate.now())
                .build();

        enqService.create(enqSBoxTest1);
        enqService.create(enqSBoxTest2);

        Fav tmpFav = Fav.builder()
                .member(newMember)
                        .enq(enqSBoxTest1)
                                .build();

        favRepository.save(tmpFav);


        Ans ans1 = Ans.builder()
                .enq(enq)
                .isShow(true)
                .member(newMember)
                .abox(abox)
                .status(AnsStatus.SAVE)
                .updateDate(LocalDate.now())
                .build();

        Ans ans2 = Ans.builder()
                .enq(enq)
                .isShow(true)
                .member(newMember)
                .abox(abox)
                .status(AnsStatus.SUBMIT)
                .updateDate(LocalDate.now())
                .build();

        ansService.save(ans1);
        ansService.save(ans2);

        Abox abox1 = Abox.builder().member(newMember).name("챗봇").build();
        Abox abox2 = Abox.builder().member(newMember).name("데관기").build();
        Abox abox3 = Abox.builder().member(newMember).name("SA").build();
        aboxService.create(abox1);
        aboxService.create(abox2);
        aboxService.create(abox3);

//        MemberResponseDTO signupResultByMemberResponseDTO = MemberResponseDTO.builder()
//                .id(newMember.getId())
//                .email(newMember.getEmail())
//                .build();
//        return signupResultByMemberResponseDTO;
    }
    public TokenDTO login(MemberLoginReqDTO memberLoginReqDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = memberLoginReqDTO.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDTO(authentication);
    }


}