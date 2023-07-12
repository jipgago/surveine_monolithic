package com.surveine.controller;

import com.surveine.config.Result;
import com.surveine.config.SecurityUtil;
import com.surveine.dto.member.MemberPageRspDTO;
import com.surveine.dto.member.MemberRegisterRspDTO;
import com.surveine.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

//    @GetMapping("/me")
//    public ResponseEntity<MemberRegisterRspDTO> getMyMemberInfo() {
//        MemberRegisterRspDTO myInfoBySecurity = memberService.getMyInfoBySecurity();
//        System.out.println(myInfoBySecurity.getEmail());
//        return ResponseEntity.ok((myInfoBySecurity));
//        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
//    }

    @GetMapping("/profile")
    public ResponseEntity<Result> memberPage() {
        Map<String, Object> rspMap = memberService.getMemberProfile();

        Result result = Result.builder()
                .isSuccess(true)
                .message("조회 성공")
                .result(rspMap)
                .build();

        return ResponseEntity.ok(result);
    }
}
