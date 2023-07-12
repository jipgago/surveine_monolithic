package com.surveine.controller;

import com.surveine.config.Result;
import com.surveine.dto.member.*;
//import com.surveine.dto.member.MemberRegisterReqDTO;
//import com.surveine.dto.member.TokenDTO;
import com.surveine.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 사용자 인증 관련 컨트롤러
 *
 * @Author: KAFEine
 * @Date: 2023.05.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 회원가입
     *
     * @param requestDTO
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<Result> signup(@RequestBody MemberRegisterReqDTO requestDTO) {
        try {
            authService.signup(requestDTO);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("회원가입 요청 성공")
                    .build();
            return ResponseEntity.ok().body(result);

        } catch (Exception exception) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("일시적인 회원가입 오류가 발생했습니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

    }

    /**
     * 로그인
     *
     * @param memberLoginReqDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody MemberLoginReqDTO memberLoginReqDTO) {
        try{
            try{
                TokenDTO tokenDTO = authService.login(memberLoginReqDTO);
                Result result = Result.builder()
                        .isSuccess(true)
                        .message("로그인 성공")
                        .result(tokenDTO)
                        .build();
                return ResponseEntity.ok().body(result);
            }catch (Exception exception) {
                Result result = Result.builder()
                        .isSuccess(false)
                        .message("로그인 실패")
                        .result(0)
                        .build();
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception exception) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("로그인 오류가 발생했습니다.")
                    .result(null)
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

    }

}
