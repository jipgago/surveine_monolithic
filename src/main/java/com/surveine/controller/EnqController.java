package com.surveine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.config.Result;
import com.surveine.config.SecurityUtil;
import com.surveine.domain.Ans;
import com.surveine.domain.Cbox;
import com.surveine.domain.Enq;
import com.surveine.domain.Member;
import com.surveine.dto.ans.AnsRspDTO;
import com.surveine.dto.ans.AnsUrlDTO;
import com.surveine.dto.enq.*;
import com.surveine.dto.member.MemberRegisterRspDTO;
import com.surveine.service.EnqService;
import com.surveine.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 개별 설문지 관련 컨트롤러
 */
@RestController
@RequestMapping("/api/enq")
public class EnqController {

    private final EnqService enqService;
    private final MemberService memberService;

    @Autowired
    public EnqController(EnqService enqService, MemberService memberService) {
        this.enqService = enqService;
        this.memberService = memberService;
    }

    /**
     * 1. 개별 설문지 조회(불러오기)
     * @param enqId
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/{enqId}")
    public ResponseEntity<Result> getEnqById(@PathVariable Long enqId) throws JsonProcessingException {
        Optional<Enq> enq = enqService.getEnqById(enqId);
        if (enq.isPresent()) {
            EnqRspDTO enqRspDTO = EnqRspDTO.builder()
                    .enq(enq.get())
                    .build();
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문 호출")
                    .result(enqRspDTO)
                    .build();
            return ResponseEntity.ok(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("워크스페이스 호출 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
    //생성하는 메서드

    /**
     * 2. 개별 설문지 저장 (첫번째 저장)
     * @param rsqDTO
     * @return
     * @throws JsonProcessingException
     */
    @PutMapping("/save")
    public ResponseEntity<Result> generateEnq(@RequestBody EnqCreateDTO rsqDTO) throws JsonProcessingException {
        if (rsqDTO != null)
        {
            Long enqId = enqService.createEnq(rsqDTO);
            if(enqId > 0L){
                EnqCreateRspDTO createRspDTO = EnqCreateRspDTO.builder()
                        .enqName(rsqDTO.getEnqName())
                        .enqTitle(rsqDTO.getEnqTitle())
                        .cboxId(rsqDTO.getCboxId())
                        .cont(rsqDTO.getEnqCont())
                        .enqId(enqId)
                        .build();
                Result result = Result.builder()
                        .isSuccess(true)
                        .message("생성 성공")
                        .result(createRspDTO)
                        .build();
                return ResponseEntity.ok(result);
            } else {
                Result result = Result.builder()
                        .isSuccess(false)
                        .message("생성 실패")
                        .build();
                return ResponseEntity.badRequest().body(result);
            }
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("생성 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 3. 개별 설문지 수정 (재저장)
     * @param enqId
     * @param enqUpdateDTO
     * @return
     * @throws JsonProcessingException
     */
    @PutMapping("/update/{enqId}")
    public ResponseEntity<Result> updateDataProcess(@PathVariable Long enqId, @RequestBody EnqUpdateDTO enqUpdateDTO) throws JsonProcessingException {
        Optional<Enq> enq = enqService.getEnqById(enqId);
        if(enq.isPresent() && enqUpdateDTO != null){
            enqUpdateDTO.toBuilder().enqId(enqId).build();
            if(enqService.updateEnq(enqUpdateDTO)){
                Result result = Result.builder()
                        .isSuccess(true)
                        .result(enqUpdateDTO)
                        .message("업데이트 성공")
                        .build();
                return ResponseEntity.ok(result);
            } else {
                Result result = Result.builder()
                        .isSuccess(false)
                        .message("업데이트 실패")
                        .build();
                return ResponseEntity.badRequest().body(result);
            }
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("업데이트 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 4. 개별 설문지 삭제
     * @param enqID
     * @return
     */
    @DeleteMapping("/delete/{enqID}")
    public ResponseEntity<Result> enqDelete(@PathVariable Long enqID) {
        Optional<Enq> enquete = enqService.getEnqById(enqID);
        Long enqMemberId = enquete.get().getMember().getId();
        if(enquete.isPresent() && enqMemberId == SecurityUtil.getCurrentMemberId()){
            enqService.deleteEnq(enqID);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("삭제 성공")
                    .build();
            return ResponseEntity.ok(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("삭제 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }



    /**
     * 응답지 불러오기
     */
    @GetMapping("/url/{url}")
    public ResponseEntity<Result> getAns(@PathVariable String url) throws JsonProcessingException {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Optional<Enq> enq = enqService.getEnqByDistLink(url);
        if(enq.isPresent()){
            AnsUrlDTO ansRspDTO = AnsUrlDTO.builder()
                    .enq(enq.get())
                    .member_id(memberId)
                    .build();

            Result result = Result.builder()
                    .isSuccess(true)
                    .message("응답지 불러오기 성공")
                    .result(ansRspDTO)
                    .build();
            return ResponseEntity.ok(result);

        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답지 불러오기 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }



}

