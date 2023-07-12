package com.surveine.controller;



import com.surveine.dto.ans.AnsDTO;
import com.surveine.dto.ans.AnsUpdateDTO;
import com.surveine.dto.ans.AnsRspDTO;
import com.surveine.repository.EnqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import com.surveine.service.AnsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.surveine.config.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wspace/ans")
@RequiredArgsConstructor
public class AnsController {
    private final AnsService ansService;
    private final EnqRepository enqRepository;

    //원래 repository 였던것을 지우고 service로 바꿨습니다.
//    @Autowired
//    public AnsController(AnsService ansService) {
//        this.ansService = ansService;
//    }

    /**
     * 2. 개별 응답 삭제
     *
     * @param ansId
     */
    @PutMapping("/delete/{ansId}")
    public ResponseEntity<Result> ansDelete(@PathVariable Long ansId){
//        Long memberId = SecurityUtil.getCurrentMemberId();
        ansService.AnsDeleteReq(ansId);

        Result result = Result.builder()
                .isSuccess(true)
                .message("삭제 성공")
                .build();
        return ResponseEntity.ok().body(result);
    }


    /**
     * 1. 응답 저장
     * @param ansDTO
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/save")
    public ResponseEntity<Result> generateAns(@RequestBody AnsDTO ansDTO) throws JsonProcessingException {
        Long ansId = ansService.createAns(ansDTO);
        System.out.println(ansDTO.toString());
        System.out.println(ansId);
        if(ansId != 0L){
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("응답 저장 성공")
                    .result(AnsRspDTO.builder()
                            .ansCont(ansDTO.getAnsCont())
                            .id(ansId)
                            .build())
                    .build();

//            ansService.addAnalysis(ansDTO);
            return ResponseEntity.ok().body(result);
        } else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답 저장 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
    @GetMapping("/{ansId}")
    public ResponseEntity<Result> getAns(@PathVariable Long ansId) throws JsonProcessingException {
        AnsRspDTO ansRspDTO = ansService.getAns(ansId);
        if(ansRspDTO != null){
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("불러오기 성공")
                    .result(ansRspDTO)
                    .build();
            return ResponseEntity.ok().body(result);
        } else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("불러오기에 실패했습니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
//    @PutMapping("/update/{ansId}")
//    public ResponseEntity<Result> updateAns(@PathVariable Long ansId, @RequestBody AnsDTO reqDto) throws JsonProcessingException {
//        reqDto.toBuilder().id(ansId).build();
//        AnsRspDTO rspDTO = ansService.updateAns(reqDto);
//        if(rspDTO != null){
//            Result result = Result.builder()
//                    .message("업데이트 성공")
//                    .isSuccess(true)
//                    .result(rspDTO)
//                    .build();
//            return ResponseEntity.ok().body(result);
//        } else{
//            Result result = Result.builder()
//                    .message("업데이트 실패")
//                    .result(null)
//                    .isSuccess(false)
//                    .build();
//            return ResponseEntity.badRequest().body(result);
//        }
//    }
    @PutMapping("/submit")
    public ResponseEntity<Result> submitAns(@RequestBody AnsDTO reqDto) throws JsonProcessingException {
       AnsRspDTO rspDTO = ansService.submitAns(reqDto);
       if(rspDTO != null){
           Result result = Result.builder()
                   .isSuccess(true)
                   .result(rspDTO)
                   .message("제출이 완료되었습니다.")
                   .build();
//           AnsUpdateDTO analysis = AnsUpdateDTO.builder()
//                   .ansId(reqDto.getId())
//                   .ansCont(reqDto.getAnsCont())
//                   .build();
//           ansService.addAnalysis(analysis);
           return ResponseEntity.ok().body(result);
       } else {
           Result result = Result.builder()
                   .result(rspDTO)
                   .isSuccess(false)
                   .message("제출 실패")
                   .build();
           return ResponseEntity.badRequest().body(result);
       }
    }

    @PutMapping("/update/{ansID}")
    public ResponseEntity<Result> updateAnswer(@PathVariable Long ansID, @RequestBody AnsUpdateDTO ansUpdateDTO) throws JsonProcessingException {
        if(ansID == ansUpdateDTO.getAnsId()){
            ansService.updateAns(ansUpdateDTO);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("업데이트 성공")
                    .build();
            return ResponseEntity.ok(result);
        } else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("업데이트 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

}
