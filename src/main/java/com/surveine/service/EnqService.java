package com.surveine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.config.SecurityUtil;
import com.surveine.domain.Cbox;
import com.surveine.domain.Enq;
import com.surveine.domain.Member;
import com.surveine.dto.enq.EnqCreateDTO;
import com.surveine.dto.enq.EnqGPSDistDTO;
import com.surveine.dto.enq.EnqUpdateDTO;
import com.surveine.dto.enq.EnqLinkDistDTO;
import com.surveine.dto.enq.enqcontents.EnqContDTO;
import com.surveine.enums.DistType;
import com.surveine.enums.EnqStatus;
import com.surveine.repository.CboxRepository;
import com.surveine.repository.EnqRepository;
import com.surveine.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnqService {
    private final EnqRepository enqRepository;
    private final MemberRepository memberRepository;
    private final CboxRepository cboxRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Enq> getEnqById(Long enqId) {
        return enqRepository.findById(enqId);
    }

    public Optional<Member> getEnqByMemberId(Long memberId) {return memberRepository.findById(memberId);}
    public Optional<Cbox> getEnqByCBoxId(Long cboxId) {return cboxRepository.findById(cboxId);}
    public List<Enq> getEnqsByCBoxId(Long defaultcboxId) {
        return enqRepository.findByCboxId(defaultcboxId);
    }

    public Optional<Enq> getEnqByDistLink(String distLink) {
        return enqRepository.findByDistLink(distLink);
    }

    /**
     * Json형 String으로 변경
     * @param enqCont
     * @return
     * @throws JsonProcessingException
     */
    public static String setEnqCont(List<EnqContDTO> enqCont) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(enqCont);
    }

    /**
     * Enq Content 맵핑
     * @param enquete
     * @return
     * @throws JsonProcessingException
     */
    public static List<EnqContDTO> getEnqCont(Enq enquete) throws JsonProcessingException {
        if (enquete.getCont() == null) {
            return Collections.emptyList();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(enquete.getCont(), new TypeReference<List<EnqContDTO>>() {});
        }
    }

    /**
     * 설문 생성 함수
     *
     * @param rsqDTO,memberId
     * @throws JsonProcessingException
     */
    @Transactional
    public Long createEnq(EnqCreateDTO rsqDTO) throws JsonProcessingException {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member nowMember = memberRepository.getOne(currentMemberId);
        Cbox nowCbox = cboxRepository.getOne(rsqDTO.getCboxId());
        if(nowCbox.getMember().getId() == currentMemberId){
            Enq enquete = Enq.builder()
                    .member(nowMember)
                    .cbox(nowCbox)
                    .name(rsqDTO.getEnqName())
                    .title(rsqDTO.getEnqTitle())
                    .cont(setEnqCont(rsqDTO.getEnqCont()))
                    .isShared(false)
                    .favCount(0L)
                    .enqStatus(EnqStatus.ENQ_MAKE)
                    .distType(DistType.LINK)
                    .updateDate(LocalDate.now())
                    .enqReport(0L)
                    .build();
            enqRepository.save(enquete);
            return enquete.getId();
        } else {
            return 0L;
        }

    }

    /**
     * 설문 복제 함수
     * @param enqId
     */
    @Transactional
    public void replicaEnq(Long enqId){
        Enq enq = enqRepository.findById(enqId)
                .orElseThrow(() -> new EntityNotFoundException("Enquete not found"));
        enq.toBuilder().id(null)
                .favCount(0L)
                .isShared(false)
                .enqStatus(EnqStatus.ENQ_MAKE)
                .updateDate(LocalDate.now())
                .build();

        enqRepository.save(enq);
    }

    /**
     * 설문 수정 함수
     * @param enqueteDto
     * @throws JsonProcessingException
     */
    @Transactional
    public boolean updateEnq(EnqUpdateDTO enqueteDto) throws JsonProcessingException {
        Optional<Enq> reqEnq = enqRepository.findById(enqueteDto.getEnqId());
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        if(currentMemberId == reqEnq.get().getMember().getId() && currentMemberId == reqEnq.get().getCbox().getMember().getId()){
            Enq enq = reqEnq.get()
                    .toBuilder()
                    .name(enqueteDto.getEnqName())
                    .cont(setEnqCont(enqueteDto.getEnqCont()))
                    .build();
            enqRepository.save(enq);
            return true;
        } else return false;
    }

    /**
     * 설문 삭제 함수
     * @param enqId
     */
    @Transactional
    public void deleteEnq(Long enqId) {
        Enq enquete = enqRepository.getOne(enqId);
        enqRepository.delete(enquete);
    }


    /**
     * 설문 제작함 설문지 폴더 이동 함수
     */
    @Transactional
    public void moveEnq(Long cboxId, Enq nowEnq){
        Cbox moveCbox = cboxRepository.getOne(cboxId);

        Enq modifiedEnq = nowEnq.toBuilder()
                .cbox(moveCbox)
                .build();

        enqRepository.save(modifiedEnq);
    }

    public Long getCountBycboxId(Long CboxId) {
        return enqRepository.countByCboxId(CboxId);
    }

    @Transactional
    public Map<String, Object> enqShare(Long enqId) {
        Map<String, Object> enqShareRspMap = new HashMap<>();

        Optional<Enq> optionalEnq = enqRepository.findById(enqId);
        Enq nowEnq = optionalEnq.get();

        Enq modified = nowEnq.toBuilder()
                .isShared(!nowEnq.getIsShared())
                .build();

        enqRepository.save(modified);

        enqShareRspMap.put("enqId", nowEnq.getId());
        enqShareRspMap.put("isShared", modified.getIsShared());

        return enqShareRspMap;
    }

    public void create(Enq enq) {
        enqRepository.save(enq);
    }

    public void enqChangeName(Enq modifiedEnq) {
        enqRepository.save(modifiedEnq);
    }



    /**
     * 즉시 배포
     */
    @Transactional
    public void enqLinkDist(Enq enq, Map<String, Object> enqDistMap) {
        //예외처리는 controller에서 해서 할 필요가 없음.
        String enqStatus = (String) enqDistMap.get("enqStatus");
        /**
         //이전 설문지 아이디를 가져온다.
         //상태 종류. 1) 제작중 -> 배포 예약 -> 배포 완료 -> 배포 마감
         //                           -> 배포 취소(제작중)
         //                 -> 배포 완료 -> 배포 마감
         */
        if(enqStatus.equals(EnqStatus.ENQ_MAKE.toString())){ // (어떤 상태) -> 배포 취소
            enq = enq.toBuilder()
                    .enqStatus(EnqStatus.valueOf(enqStatus))
                    .quota(null)
                    .startDateTime(null)
                    .endDateTime(null)
                    .ansedCnt(null)
                    .distLink(null)
                    .my_location(null)
                    .distRange(null)
                    .build();
        }else { // (어떤 상태) -> 배포 완료, 배포 마감
            //배포 테이블 업데이트
            Map<String, Object> distInfo = (Map<String, Object>) enqDistMap.get("distInfo");
            String quota = (String) distInfo.get("quota");
            LocalDate startDate = LocalDate.parse((String) distInfo.get("startDate"));
            LocalTime startTime = LocalTime.parse((String) distInfo.get("startTime"));
            LocalDate endDate = LocalDate.parse((String) distInfo.get("endDate"));
            LocalTime endTime = LocalTime.parse((String) distInfo.get("endTime"));
            String distLink = (String) distInfo.get("distLink");

            enq = enq.toBuilder()
                    .enqStatus(EnqStatus.valueOf(enqStatus))
                    .quota(Integer.parseInt(quota))
                    .startDateTime(LocalDateTime.of(startDate, startTime))
                    .endDateTime(LocalDateTime.of(endDate, endTime))
                    .ansedCnt(enq.getAnsedCnt())
                    .distLink(distLink)
                    .build();
        }
        enqRepository.save(enq);
    }

    @Transactional
    public void enqGPSDist(Enq enq, Map<String, Object> enqDistMap) {
        //예외처리는 controller에서 해서 할 필요가 없음.
        String enqStatus = (String) enqDistMap.get("enqStatus");
        /**
         //이전 설문지 아이디를 가져온다.
         //상태 종류. 1) 제작중 -> 배포 예약 -> 배포 완료 -> 배포 마감
         //                           -> 배포 취소(제작중)
         //                 -> 배포 완료 -> 배포 마감
         */
        if(enqStatus.equals(EnqStatus.ENQ_MAKE)){ // (어떤 상태) -> 배포 취소
            enq.toBuilder()
                    .enqStatus(EnqStatus.valueOf(enqStatus))
                    .quota(null)
                    .startDateTime(null)
                    .endDateTime(null)
                    .ansedCnt(null)
                    .distLink(null)
                    .my_location(null)
                    .distRange(null)
                    .build();
        }else { // (어떤 상태) -> 배포 완료, 배포 마감
            //배포 테이블 업데이트
            Map<String, Object> distInfo = (Map<String, Object>) enqDistMap.get("distInfo");
            int quota = (int) distInfo.get("quota");
            LocalDate startDate = LocalDate.parse((String) distInfo.get("startDate"));
            LocalTime startTime = LocalTime.parse((String) distInfo.get("startTime"));
            LocalDate endDate = LocalDate.parse((String) distInfo.get("endDate"));
            LocalTime endTime = LocalTime.parse((String) distInfo.get("endTime"));
            int x = (int) distInfo.get("x");
            int y = (int) distInfo.get("y");

            enq.toBuilder()
                    .enqStatus(EnqStatus.valueOf(enqStatus))
                    .quota(quota)
                    .startDateTime(LocalDateTime.of(startDate, startTime))
                    .endDateTime(LocalDateTime.of(endDate, endTime))
                    .ansedCnt(enq.getAnsedCnt())
                    .my_location(new Point(x, y))
                    .build();
        }
        enqRepository.save(enq);
//        //예외처리는 controller에서 해서 할 필요가 없음.
//        String enqStatus = (String) enqDistMap.get("enqStatus");
//        EnqGPSDistDTO enqDistDTO = (EnqGPSDistDTO) enqDistMap.get("distInfo");
//        /**
//         //이전 설문지 아이디를 가져온다.
//         //상태 종류. 1) 제작중 -> 배포 예약 -> 배포 완료 -> 배포 마감
//         //                           -> 배포 취소(제작중)
//         //                 -> 배포 완료 -> 배포 마감
//         */
//        if(enqStatus.equals(EnqStatus.ENQ_MAKE)){ // (어떤 상태) -> 배포 취소
//            enq.toBuilder()
//                    .enqStatus(EnqStatus.valueOf(enqStatus))
//                    .quota(null)
//                    .startDateTime(null)
//                    .endDateTime(null)
//                    .ansedCnt(null)
//                    .distLink(null)
//                    .my_location(null)
//                    .distRange(null)
//                    .build();
//        }else { // (어떤 상태) -> 배포 완료, 배포 마감
//            //배포 테이블 업데이트
//            enq.toBuilder()
//                    .enqStatus(EnqStatus.valueOf(enqStatus))
//                    .quota(enqDistDTO.getQuota())
//                    .startDateTime(enqDistDTO.getStartDate())
//                    .endDate(enqDistDTO.getEndDate())
//                    .ansedCnt(enq.getAnsedCnt())
//                    .my_location(new Point(enqDistDTO.getX(), enqDistDTO.getY()))
//                    .build();
//        }
//        enqRepository.save(enq);
    }

    @Transactional
    @Scheduled(fixedDelay = 1) // 0.001초마다 실행
    public void processDistribution() {
        int batchSize = 10;

//        // enqStatus가 "DIST_WAIT"인 테이블 목록을 startDate 순으로 정렬하여 최대 10개 가져오기
//        List<Enq> enqList = enqRepository.findTop10ByEnqStatusOrderByStartDate("DIST_WAIT");

        // distType이 "LINK"이고 enqStatus가 "DIST_WAIT"인 테이블 목록을 startDate 순으로 정렬하여 최대 10개 가져오기
        List<Enq> enqList = enqRepository.findTop10ByDistTypeAndEnqStatusOrderByStartDateTime("LINK", "DIST_WAIT");

        if (enqList.size() == batchSize) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime tenthEnqStartDate = enqList.get(batchSize - 1).getStartDateTime();

            // 10번째의 startDate가 현재 시간보다 앞이면 enqStatus를 "DIST_WAIT"에서 "DIST_DONE"으로 변경
            if (tenthEnqStartDate.isBefore(currentDateTime)) {
                for (Enq enq : enqList) {
                    enq.toBuilder().enqStatus(EnqStatus.valueOf("DIST_DONE")).build();
                }
                enqRepository.saveAll(enqList);
            }
        }
    }


    @Transactional
    public void updateEnqState(Enq enq, String enqStatus) {
        // 배포 예약 -> 배포 시작일 경우: 배포 시작 날짜를 지금으로 바꿔야 함.
        if(enqStatus.equals(EnqStatus.DIST_DONE.toString())){
            enq = enq.toBuilder().enqStatus(EnqStatus.valueOf(enqStatus)).startDateTime(LocalDateTime.now()).build();
            enqRepository.save(enq);
        }
        // 배포 종료일 경우: 배포 종료 날짜를 지금으로 바꿔야 함.
        else if(enqStatus.equals(EnqStatus.ENQ_DONE.toString())){
            enq = enq.toBuilder().enqStatus(EnqStatus.valueOf(enqStatus)).endDateTime(LocalDateTime.now()).build();
            enqRepository.save(enq);
        }
    }


}
