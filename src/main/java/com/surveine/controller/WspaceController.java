package com.surveine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.config.Result;
import com.surveine.config.SecurityUtil;
import com.surveine.domain.*;
import com.surveine.dto.abox.AboxNameDTO;
import com.surveine.dto.abox.AboxWspaceDTO;
import com.surveine.dto.ans.AnsMoveDTO;
import com.surveine.dto.ans.AnsWspaceDTO;
import com.surveine.dto.cbox.CboxDTO;
import com.surveine.dto.cbox.CboxNameDTO;
import com.surveine.dto.cbox.CboxWSpaceDTO;
import com.surveine.dto.cbox.EnqNameChangeDTO;
import com.surveine.dto.enq.EnqMoveDTO;
import com.surveine.dto.enq.EnqStatusDTO;
import com.surveine.dto.enq.EnqUpdateDTO;
import com.surveine.dto.enq.EnqWSpaceDTO;
import com.surveine.dto.abox.AboxDTO;
import com.surveine.enums.DistType;
import com.surveine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//1@1.com  kafeine! or kafeine1~!
/**
 * 워크스페이스 관련 컨트롤러
 *
 * @Author: KAFEine
 * @Date: 2023.05.06
 */
@RestController
@RequestMapping("/api/wspace")
public class WspaceController {
    private final MemberService memberService;
    private final CboxService cboxService;
    private final AboxService aboxService;
    private final EnqService enqService;
    private final AnsService ansService;

    @Autowired
    public WspaceController(CboxService cboxService, AboxService aboxService, EnqService enqService, AnsService ansService, MemberService memberService) {
        this.cboxService = cboxService;
        this.aboxService = aboxService;
        this.enqService = enqService;
        this.ansService = ansService;
        this.memberService = memberService;
    }

    /**
     * 0. 워크스페이스 첫 렌더링 (default Cbox 호출)
     * 1. 해당 제작함 폴더 설문지리스트 조회
     * 워크스페이스 Cbox 커피콩 조회 + 워크스페이스 첫 렌더링 (Member-Default-Cbox 호출)
     *
     * @PathVariable cboxId
     */
    @GetMapping("/cbox/{cboxId}")
    public ResponseEntity<Result> wspaceCboxPage(@PathVariable(required = false) Long cboxId) { // cboxId가 null이면 0으로 처리, 잘 안되면 String으로
        Long memberId = SecurityUtil.getCurrentMemberId();

        List<Cbox> cboxList = cboxService.getCBoxesByMemberId(memberId);
        List<CboxDTO> cboxDTOList = cboxList.stream()
                .map(cbox -> CboxDTO.builder()
                        .cbox(cbox)
                        .enqCnt(enqService.getCountBycboxId(cbox.getId()))
                        .build())
                .collect(Collectors.toList());

        if (cboxDTOList.isEmpty()) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("워크스페이스 네비게이션 호출 실패 - cbox 없음! ")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

        List<Abox> aboxList = aboxService.getAboxesByMemberId(memberId);
        List<AboxDTO> aboxDTOList = aboxList.stream()
                .map(abox -> AboxDTO.builder()
                        .abox(abox)
                        .ansCnt(ansService.getCountByaboxId(abox.getId()))
                        .build())
                .collect(Collectors.toList());

        if (aboxDTOList.isEmpty()) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("워크스페이스 네비게이션 호출 실패 - Abox 없음! ")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

        if (cboxId == 0) { //cboxId가 null이면 0으로 처리(사용자의 첫 cboxId로 처리), 잘 안되면 String으로 바꿀 예정입니다.
            cboxId = cboxDTOList.get(0).getCboxId();
        }

        List<Enq> enqs = enqService.getEnqsByCBoxId(cboxId);
        List<EnqWSpaceDTO> enqWSpaceDTOList = enqs.stream()
                .map(enq -> EnqWSpaceDTO.builder()
                        .enq(enq)
                        .build())
                .collect(Collectors.toList());

        Optional<Cbox> cbox = cboxService.getCBoxById(cboxId);
        CboxWSpaceDTO cboxWSpaceDTO = CboxWSpaceDTO.builder()
                .cbox(cbox.get())
                .enqList(enqWSpaceDTOList)
                .build();

        Map<String, Object> workspaceMap = new HashMap<>();
        workspaceMap.put("memberName", memberService.getNameById(memberId));
        workspaceMap.put("cboxList", cboxDTOList);
        workspaceMap.put("aboxList", aboxDTOList);
        workspaceMap.put("cbox", cboxWSpaceDTO);

        Result result = Result.builder()
                .isSuccess(true)
                .message("워크스페이스 호출 성공")
                .result(workspaceMap)
                .build();

        return ResponseEntity.ok().body(result);
    }



    /**
     * 2. 해당 참여함 폴더 설문지리스트 조회
     *
     * @PathVariable aboxId
     */
    @GetMapping("/abox/{aboxId}")
    public ResponseEntity<Result> wspaceAboxPage(@PathVariable Long aboxId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        List<Cbox> cboxList = cboxService.getCBoxesByMemberId(memberId);
        List<CboxDTO> cboxDTOList = cboxList.stream()
                .map(cbox -> CboxDTO.builder()
                        .cbox(cbox)
                        .build())
                .collect(Collectors.toList());

        if (cboxDTOList.isEmpty()) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("워크스페이스 네비게이션 호출 실패 - cbox 없음! ")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

        List<Abox> aboxList = aboxService.getAboxesByMemberId(memberId);
        List<AboxDTO> aboxDTOList = aboxList.stream()
                .map(abox -> AboxDTO.builder()
                        .abox(abox)
                        .build())
                .collect(Collectors.toList());

        if (aboxDTOList.isEmpty()) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("워크스페이스 네비게이션 호출 실패 - Abox 없음! ")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

        List<Ans> anslist = ansService.getAnsByAboxId(aboxId);
        List<AnsWspaceDTO> ansWspaceDTOList = anslist.stream()
                .filter(ans -> ans.getIsShow())
                .map(ans -> AnsWspaceDTO.builder()
                        .ans(ans)
                        .build())
                .collect(Collectors.toList());

        Optional<Abox> abox = aboxService.getAboxById(aboxId);
        AboxWspaceDTO aboxWspaceDTO = AboxWspaceDTO.builder()
                .abox(abox.get())
                .ansList(ansWspaceDTOList)
                .build();

        Map<String, Object> workspaceMap = new HashMap<>();
        workspaceMap.put("memberName", memberService.getNameById(memberId));
        workspaceMap.put("cboxList", cboxDTOList);
        workspaceMap.put("aboxList", aboxDTOList);
        workspaceMap.put("abox", aboxWspaceDTO);

        Result result = Result.builder()
                .isSuccess(true)
                .message("워크스페이스 호출 성공")
                .result(workspaceMap)
                .build();

        return ResponseEntity.ok().body(result);
    }

    /**
     * 3. GPS 설문함 전체 설문지 조회
     *
     */
    @PostMapping("/gps")
    public ResponseEntity<Result> wspaceGpsPage() {
        //TODO: GPS 설문함 전체 설문지 조회
        return null;
    }


    /**
     * 4. 사이드네비 제작함 폴더 생성
     *
     * @RequestBody CboxNameDTO
     */
    @PostMapping("/cbox/new")
    public ResponseEntity<Result> wspaceAddCbox(@RequestBody CboxNameDTO cboxNameDTO) {
//        MemberRegisterRspDTO myInfoBySecurity = memberService.getMyInfoBySecurity();
//        Long memberId = myInfoBySecurity.getId();
        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberService.getOneMemberById(memberId);
        Cbox cbox = Cbox.builder()
                .name(cboxNameDTO.getCboxName())
                .member(member)
                .build();
        cboxService.create(cbox);
        Result result = Result.builder()
                .isSuccess(true)
                .message("새 Cbox 폴더 생성 성공")
                .build();
        return ResponseEntity.ok().body(result);
    }

    /**
     * 5. 사이드네비 참여함 폴더 생성
     *
     * @RequestBody AboxNameDTO
     */
    @PostMapping("/abox/new")
    public ResponseEntity<Result> wspaceAddAbox(@RequestBody AboxNameDTO aboxNameDTO) {
//        MemberRegisterRspDTO myInfoBySecurity = memberService.getMyInfoBySecurity();
//        Long memberId = myInfoBySecurity.getId();
        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberService.getOneMemberById(memberId);
        Abox abox = Abox.builder()
                .name(aboxNameDTO.getAboxName())
                .member(member)
                .build();
        aboxService.create(abox);
        Result result = Result.builder()
                .isSuccess(true)
                .message("새 Abox 폴더 생성 성공")
                .build();
        return ResponseEntity.ok().body(result);
    }

    /**
     * 6. 사이드네비 제작함폴더 이름 변경
     *
     * @RequestBody CboxNameDTO
     */
    @PutMapping("/cbox/rename/{cboxId}")
    public ResponseEntity<Result> wspaceEditCboxName(@RequestBody CboxNameDTO cboxNameDTO, @PathVariable Long cboxId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberService.getOneMemberById(memberId);
        Cbox cbox = cboxService.findById(cboxId).get();
        Cbox modifiedCbox = cbox.toBuilder()
                .name(cboxNameDTO.getCboxName())
                .member(member)
                .build();
        cboxService.create(modifiedCbox);
        Result result = Result.builder()
                .isSuccess(true)
                .message("Cbox 이름 변경 성공")
                .build();
        return ResponseEntity.ok().body(result);

    }

    /**
     * 7. 사이드네비 참여함 폴더 이름 변경
     *
     * @RequestBody AboxNameDTO
     */
    @PutMapping("/abox/rename/{aboxId}")
    public ResponseEntity<Result> wspaceEditAboxName(@RequestBody AboxNameDTO aboxNameDTO, @PathVariable Long aboxId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberService.getOneMemberById(memberId);
        Abox abox = aboxService.getAboxById(aboxId).get();
        Abox modifiedAbox = abox.toBuilder()
                .name(aboxNameDTO.getAboxName())
                .member(member)
                .build();
        aboxService.create(modifiedAbox);
        Result result = Result.builder()
                .isSuccess(true)
                .message("Abox 이름 변경 성공")
                .build();
        return ResponseEntity.ok().body(result);
    }


    /**
     * 8. 사이드네비 제작함폴더 삭제
     *
     * @PathVariable cboxId
     * @RequestBody CboxNameDTO
     */
    @DeleteMapping("/cbox/delete/{cboxId}")
    public ResponseEntity<Result> wspaceDeleteCbox(@PathVariable Long cboxId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Boolean rspBoolean = cboxService.deleteBox(cboxId);

        if (rspBoolean) {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("Cbox 삭제 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("Cbox 삭제 실패. 폴더에 설문이 존재합니다.")
                    .build();
            return ResponseEntity.ok().body(result);
        }
    }

    /**
     * 9. 사이드네비 참여함폴더 삭제
     *
     * @PathVariable aboxId
     * @RequestBody AboxNameDTO
     */
    @DeleteMapping("/abox/delete/{aboxId}")
    public ResponseEntity<Result> wspaceDeleteAbox(@PathVariable Long aboxId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Boolean rspBoolean = aboxService.deleteBox(aboxId);

        if (rspBoolean) {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("Abox 삭제 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("Abox 삭제 실패. 폴더에 설문이 존재합니다.")
                    .build();
            return ResponseEntity.ok().body(result);
        }
    }


    /**
     * TODO: EnqController로 이동 필요. 같이 코드 정리할때 할 것.
     * TODO: 배포 api는
     * 10. 설문지 배포시작
     * @RequestBody enqDistDTO
     */
    @PutMapping("/enq/dist/{enqId}")
    public ResponseEntity<Result> enqDist(@PathVariable Long enqId, @RequestBody Map<String, Object> enqDistMap) throws JsonProcessingException {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Enq> enq = enqService.getEnqById(enqId);
        if(enq.isPresent()){
            String distType = (String) enqDistMap.get("distType");
            if(distType.equals(DistType.LINK.toString())){
                enqService.enqLinkDist(enq.get(), enqDistMap);
                ansService.defaultAnalysis(enq.get()); //결과분석 초기화
            }else{
                enqService.enqGPSDist(enq.get(), enqDistMap);
                ansService.defaultAnalysis(enq.get());//결과분석 초기화
            }

            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 배포 성공")
                    .build();
            return ResponseEntity.ok(result);

        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 배포 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);

        }

    }

    /**
     * 설문지 배포 링크 클릭
     */
    @GetMapping("/enq/url/{enqId}")
    public ResponseEntity<Result> enqDistUrl(@PathVariable Long enqId) {
        Optional<Enq> enq = enqService.getEnqById(enqId);
        String baseUrl = "surveine.com/answer/";
        if(enq.isPresent()){
            String url = baseUrl + enq.get().getDistLink();
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 url 반환 성공")
                    .result(url)
                    .build();
            return ResponseEntity.ok(result);
        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 url 반환 성공")
                    .build();
            return ResponseEntity.badRequest().body(result);

        }

    }

    /**
     * 10-1. 커피콩에서 바로 설문지 배포시작/종료
     */
    @PutMapping("/dist")
    public ResponseEntity<Result> updateEnqState(@RequestBody EnqStatusDTO enqStatusDTO) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Enq> enq = enqService.getEnqById(enqStatusDTO.getEnqId());
        if (enq.isPresent()) {
            enqService.updateEnqState(enq.get(), enqStatusDTO.getEnqStatus());
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 배포 상태 변경 성공")
                    .result(enq)
                    .build();
            return ResponseEntity.ok(result);
        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 배포 상태 변경 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

    }


    /**
     * 12. 커피콩리스트에서 설문지 이름 바꾸기
     * @PathVariable enqID
     * @RequestBody enqNameChangeDTO
     */
    @PutMapping("/enq/rename/{enqID}")
    public ResponseEntity<Result> enqNameChange(@PathVariable Long enqID, @RequestBody EnqNameChangeDTO reqDTO){
        Optional<Enq> nowEnq= enqService.getEnqById(enqID);
        Map<String, String> rspMap = new HashMap<>();

        if(nowEnq.isPresent()){
            Enq modifiedEnq = nowEnq.get().toBuilder()
                    .name(reqDTO.getEnqName()).build(); // enqID를 설정하여 DTO 객체를 업데이트함
            enqService.enqChangeName(modifiedEnq);

            rspMap.put("enqName", reqDTO.getEnqName());

            Result result = Result.builder()
                    .isSuccess(true)
                    .message("이름 변경 완료")
                    .result(rspMap)
                    .build();
            return ResponseEntity.ok().body(result);
        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("이름 변경 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }



    /**
     * 13. 커피콩리스트에서 설문지 복제
     * @PathVariable enqID
     */
    @PutMapping("/enq/replic/{enqID}")
    public ResponseEntity<Result> enqReplic(@PathVariable Long enqID){
        Optional<Enq> enquete = enqService.getEnqById(enqID);
        if(enquete.isPresent()){
            enqService.replicaEnq(enqID);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문 복제 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("복제 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 14. 커피콩리스트에서 설문지 삭제
     * @PathVariable enqID
     */
    @DeleteMapping("/enq/delete/{enqID}")
    public ResponseEntity<Result> enqDelete(@PathVariable Long enqID){
        Optional<Enq> enquete = enqService.getEnqById(enqID);
        if(enquete.isPresent()){
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



//4 에서 처리할 수 있어 사용안함.
//    /**
//     * 15. 커피콩리스트에서 응답지 삭제 (나에게만 삭제)
//     *  (boolean값만 false로)
//     *  @PathVariable ansID
//     */
//    @PutMapping("/ans/delete/{ansID}")
//    public ResponseEntity<Result> ansDelete(@PathVariable Long ansID){
//        //TODO: 삭제 시, boolean값만 false로 바꾸어 나에게만 안보이게 해야함.
//        // 아래 코드는 수정 필요한 코드. 참조하기 위해 남겨둠.
//
////        Optional<Enq> enquete = enqService.getEnqById(ansID);
////        if(enquete.isPresent()){
////            enqService.deleteEnq(ansID);
////            Result result = Result.builder()
////                    .isSuccess(true)
////                    .message("삭제 성공")
////                    .build();
////            return ResponseEntity.ok(result);
////        } else{
////            Result result = Result.builder()
////                    .isSuccess(false)
////                    .message("삭제 실패")
////                    .build();
////            return ResponseEntity.badRequest().body(result);
////        }
//        return null;
//    }


    /**
     * 16. 커피콩리스트에서 설문지 폴더 이동
     * @PathVariable enqID
     * @RequestBody enqMoveDTO
     */
    @PutMapping("/enq/move/{enqId}")
    public ResponseEntity<Result> enqMove(@PathVariable Long enqId, @RequestBody EnqMoveDTO enqMoveDTO) {

        Optional<Enq> enquete = enqService.getEnqById(enqId);
        if (enquete.isPresent()) {
            enqService.moveEnq(enqMoveDTO.getCboxId(), enquete.get());
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("이동 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("이동 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 17. 커피콩리스트에서 응답지 폴더 이동
     * @PathVariable ansID
     * @RequestBody ansMoveDTO
     */
    @PutMapping("/ans/move/{ansId}")
    public ResponseEntity<Result> ansMove(@PathVariable Long ansId, @RequestBody AnsMoveDTO ansMoveDTO) {
        Optional<Ans> ans = ansService.getAnsById(ansId);
        if (ans.isPresent()) {
            ansService.moveAns(ansMoveDTO, ansId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("폴더 이동 성공")
                    .build();
            return ResponseEntity.ok(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("폴더 이동 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 18. 커피콩리스트에서 설문지 공유 상태 변경
     *
     * - 정현진 -
     * 단순하게, isShared = !isShared 로 구현하였습니다.
     * 만약, 샌드백스 구현 간, 공유와 해제 예외처리나 Cascade가 필요하다면
     * 추가 구현하겠습니다.
     * (ex. 공유 해제 시, 좋아요 기록까지 삭제, 좋아요 기록 남기기 등)
     *
     *
     * @PathVariable enqID
     * @RequestBody enqShareDTO
     */
    @PutMapping("/enq/share/{enqId}")
    public ResponseEntity<Result> enqShare(@PathVariable Long enqId) {
        Map<String, Object> rspMap = enqService.enqShare(enqId);

        Result result = Result.builder()
                .isSuccess(true)
                .message("공유하기 상태 변경 성공")
                .result(rspMap)
                .build();

        return ResponseEntity.ok().body(result);
    }
}

