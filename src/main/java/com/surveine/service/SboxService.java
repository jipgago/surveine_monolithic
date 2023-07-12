package com.surveine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.config.SecurityUtil;
import com.surveine.domain.*;
import com.surveine.dto.cbox.CboxSboxDTO;
import com.surveine.dto.sbox.SboxEnqDTO;
import com.surveine.dto.sbox.SboxEnqReportDTO;
import com.surveine.dto.sbox.SboxFavEnqDTO;
import com.surveine.dto.sbox.SboxViewEnqDTO;
import com.surveine.enums.DistType;
import com.surveine.enums.EnqStatus;
import com.surveine.repository.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SboxService {
    private final EnqRepository enqRepository;
    private final FavRepository favRepository;
    private final CboxRepository cboxRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final MailService mailService;
    /**
     * enq가 존재하는 enq인지 확인
     * @param enqId
     * @return
     */
    public boolean isPresent(Long enqId){
        Optional<Enq> enq= enqRepository.findById(enqId);
        return enq.isPresent();
    }

    /**
     * 1. 공유된 탬플릿 리스트 제공
     * @return
     */
    public List<SboxEnqDTO> getSharedEnq(){
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<SboxEnqDTO> sharedEnq = new ArrayList<>();
        List<Enq> shareEnq = enqRepository.findByIsSharedTrue();
        for(Enq sboxEnq : shareEnq) {
            boolean chkFav = isFav(sboxEnq.getId(), memberId);
            SboxEnqDTO transform = SboxEnqDTO.builder()
                    .enqId(sboxEnq.getId())
                    .enqName(sboxEnq.getTitle())
                    .favCount(sboxEnq.getFavCount())
                    .isFav(chkFav)
                    .build();
            sharedEnq.add(transform);
        }
        return sharedEnq;
    }

    /**
     * 맴버 Cbox 가져오기
     * @return
     */
    public List<CboxSboxDTO> getMemberCbox(){
        List<CboxSboxDTO> setCbox = new ArrayList<>();
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<Cbox> cboxList = cboxRepository.findByMemberId(memberId);
        for(Cbox cbox : cboxList){
             setCbox.add(new CboxSboxDTO(cbox.getId(), cbox.getName()));
        }
        return setCbox;
    }

    /**
     * 2. 좋아요 누른 Enq 조회
     * @return
     */
    public List<SboxFavEnqDTO> getFavEnq() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<SboxFavEnqDTO> favEnqList = new ArrayList<>();
        List<Fav> fav = favRepository.findByMemberId(memberId);
        for(Fav a : fav){
            Optional<Enq> enq = enqRepository.findById(a.getEnq().getId());
            if(enq.isPresent()) {
                SboxFavEnqDTO favEnqDTO = SboxFavEnqDTO.builder()
                        .enqName(enq.get().getName())
                        .enqId(enq.get().getId())
                        .favCount(enq.get().getFavCount())
                        .isFav(true)
                        .build();
                favEnqList.add(favEnqDTO);
            }
        }
        return favEnqList;
    }

    /**
     * 샌드박스 Enq 개별 조회
     * @param enqId
     * @return
     * @throws JsonProcessingException
     */
    public SboxViewEnqDTO viewEnq(Long enqId) throws JsonProcessingException {
        Optional<Enq> enq = enqRepository.findById(enqId);
        SboxViewEnqDTO rspDTO = SboxViewEnqDTO.builder()
                .enqTitle(enq.get().getTitle())
                .enqName(enq.get().getName())
                .enqCont(EnqService.getEnqCont(enq.get()))
                .build();
        return rspDTO;
    }

    /**
     * 내 제작함으로 가져오기
     * @param cboxId
     * @param enqId
     * @return
     */
    @Transactional
    public boolean moveGetSbox(Long cboxId, Long enqId){
        Optional<Cbox> cbox = cboxRepository.findById(cboxId);
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long memberId = SecurityUtil.getCurrentMemberId();

        if(cbox.isPresent() && enq.isPresent() && memberId == cbox.get().getMember().getId()){
            Enq replica = enq.get().toBuilder()
                    .id(null)
                    .member(memberRepository.findById(memberId).get())
                    .cbox(cbox.get())
                    .favCount(0L)
                    .isShared(false)
                    .distType(DistType.LINK)
                    .updateDate(LocalDate.now())
                    .enqStatus(EnqStatus.ENQ_MAKE)
                    .build();
            enqRepository.save(replica);
            return true;
        } else {
            return false;
        }
    }

    /**
     * SboxEnq 신고
     * @param reqDto
     */
    @Transactional
    public boolean reportSboxEnq(SboxEnqReportDTO reqDto) throws MessagingException {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.getOne(memberId);
        Enq enq = enqRepository.getOne(reqDto.getEnqId());

        if(isReport(enq, memberId)){
            Report report = Report.builder()
                    .memberId(member)
                    .enqId(enq)
                    .build();
            reportRepository.save(report);
            List<Report> reports = reportRepository.findByEnqId(enq);
            Long updateEnqReport = Long.valueOf(reports.size());
            if(updateEnqReport + 1L == 10) mailService.reportSendEmail(reqDto.getEnqId());
            Enq updateEnq = enq.toBuilder().enqReport(updateEnqReport+1L).build();
            enqRepository.save(updateEnq);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fav에 자신이 Enq를 가지고 있는지 확인
     * @param enqId
     * @param memberId
     * @return
     */
    public boolean isFav(Long enqId, Long memberId) {
        List<Fav> memberFav = favRepository.findByMemberId(memberId);
        for(Fav index : memberFav){
            if(index.getEnq().getId() == enqId) return true;
        }
        return false;
    }

    public boolean isReport(Enq enq, Long memberId){

        List<Report> reports = reportRepository.findByEnqId(enq);
        for(Report index : reports){
            if(index.getMemberId().getId() == memberId) return false;
        }
        return true;
    }
}
