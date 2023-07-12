package com.surveine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.config.Result;
import com.surveine.dto.sbox.*;
import com.surveine.service.EnqService;
import com.surveine.service.MailService;
import com.surveine.service.SboxService;
import com.surveine.service.FavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/sbox")
public class SboxController {
    private final EnqService enqService;
    private final FavService favService;
    private final SboxService sboxService;
    private final MailService mailService;

    @Autowired
    public SboxController(EnqService enqService, FavService favService, SboxService sboxService, MailService mailService) {
        this.enqService = enqService;
        this.favService = favService;
        this.sboxService = sboxService;
        this.mailService = mailService;
    }

    /**
     * 1. 공유 템플릿 리스트 조회
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<Result> sandboxPage(){
        SboxEnqListDTO rspDTO = new SboxEnqListDTO(sboxService.getMemberCbox(), sboxService.getSharedEnq());
        if(!rspDTO.getSandboxCBList().isEmpty()){
            Result result = Result.builder()
                    .isSuccess(true)
                    .result(rspDTO)
                    .message("샌드박스 불러오기 완료")
                    .build();
            return ResponseEntity.ok().body(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("공유된 Enq가 없습니다.")
                    .result(rspDTO)
                    .build();
            return ResponseEntity.ok().body(result);
        }
    }

    /**
     * 2. 관심 템플릿 리스트 조회
     * @return
     *
     */
    @GetMapping("/favlist")
    public ResponseEntity<Result> viewFavoriteEnq() {
        SboxFavEnqListDTO rspDTO = new SboxFavEnqListDTO(sboxService.getMemberCbox() , sboxService.getFavEnq());

        Result result = Result.builder()
                .isSuccess(true)
                .message("fav Enq list 불러오기 성공")
                .result(rspDTO)
                .build();
        return ResponseEntity.ok().body(result);
    }

    /**
     * 3. 개별 템플릿 조회
     * @param enqId
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/{enqId}")
    public ResponseEntity<Result> viewSboxEnq(@PathVariable Long enqId) throws JsonProcessingException {
        if(sboxService.isPresent(enqId)){
            SboxViewEnqDTO rspDTO = sboxService.viewEnq(enqId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("enq 불러오기 완료")
                    .result(rspDTO)
                    .build();
            return ResponseEntity.ok().body(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("enq를 불러올 수 없습니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

    }

    /**
     * 4. 템플릿 좋아요/좋아요 취소
     * @param reqDto
     * @return
     */
    @PutMapping("/fav")
    public ResponseEntity<Result> addFavoriteEnq(@RequestBody SboxEnqFavDTO reqDto) {
        boolean handlerResult = favService.favHandler(reqDto.getEnqId());
        if(handlerResult){
            Result result = Result.builder()
                    .message("'좋아요' 활성화")
                    .isSuccess(true)
                    .build();
            return ResponseEntity.ok().body(result);
        } else {
            Result result = Result.builder()
                    .message("'좋아요' 비활성화")
                    .isSuccess(true)
                    .build();
            return ResponseEntity.ok().body(result);
        }
    }

    /**
     * 5. 템플릿 신고
     * @param reqDto
     * @return
     * @throws MessagingException
     */
    @PostMapping("/report")
    public ResponseEntity<Result> reportEnq(@RequestBody SboxEnqReportDTO reqDto) throws MessagingException {
        Long enqId = reqDto.getEnqId();
        System.out.println(enqId);
        if(sboxService.isPresent(reqDto.getEnqId())){
            if(sboxService.reportSboxEnq(reqDto)){
                Result result = Result.builder()
                        .isSuccess(true)
                        .result(reqDto)
                        .message("신고 접수가 완료되었습니다.")
                        .build();
                return ResponseEntity.ok().body(result);
            } else{
                Result result = Result.builder()
                        .isSuccess(false)
                        .message("중복된 신고접수입니다.")
                        .build();
                return ResponseEntity.badRequest().body(result);
            }
        } else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("신고접수가 불가합니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 6. 내 제작함으로 가져오기
     * @param reqDTO
     * @return
     */
    @PostMapping("/bring")
    public ResponseEntity<Result> addFavoriteEnq(@RequestBody SboxMoveMyCboxDTO reqDTO) {
        boolean exception = sboxService.moveGetSbox(reqDTO.getCboxId(), reqDTO.getEnqId());
        if(exception){
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("Enq 가져오기 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("Enq가 없거나 Cbox가 존재하지 않습니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
}
