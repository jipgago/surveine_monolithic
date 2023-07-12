package com.surveine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.config.Result;
import com.surveine.domain.Enq;
import com.surveine.dto.ans.AnsAnalysisDTO;
import com.surveine.service.AnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/result")
public class ResultController {
    private final AnsService ansService;

    @GetMapping("/{enqID}")
    public ResponseEntity<Result> getAnalysis(@PathVariable Long enqID) throws JsonProcessingException {
        AnsAnalysisDTO rspDTO = ansService.getAnalysis(enqID);
        if(rspDTO != null){
            Result result = Result.builder()
                    .message("결과 분석 불러오기 성공")
                    .isSuccess(true)
                    .result(rspDTO)
                    .build();
            return ResponseEntity.ok().body(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("결과 분석 불러오기 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
}