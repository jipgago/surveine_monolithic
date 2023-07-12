package com.surveine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.xml.bind.v2.TODO;
import com.surveine.config.SecurityUtil;
import com.surveine.domain.Abox;
import com.surveine.domain.Ans;
import com.surveine.domain.Enq;
import com.surveine.dto.ans.*;
import com.surveine.dto.ans.analysis.*;
import com.surveine.dto.enq.enqcontents.EnqContDTO;
import com.surveine.enums.AnsStatus;
import com.surveine.enums.GenderType;
import com.surveine.repository.AnsRepository;
import com.surveine.dto.ans.anscontents.AnsContDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.surveine.repository.MemberRepository;
import com.surveine.repository.AboxRepository;
import com.surveine.repository.EnqRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.domain.Member;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AnsService {
    private final AnsRepository ansRepository;
    private final MemberRepository memberRepository;
    private final AboxRepository aboxRepository;
    private final EnqRepository enqRepository;
    private final EnqService enqService;


    public Optional<Ans> getAnsById(Long ansId) {
        return ansRepository.findById(ansId);
    }

    public List<Ans> getAnsByAboxId(Long aboxId) {
        return ansRepository.findByAboxId(aboxId);
    }

    public static String setAnsCont(List<AnsContDTO> ansContDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ansContDTO);
    }

    public static List<AnsContDTO> getAnsCont(Ans answer) throws JsonProcessingException {
        if (answer.getCont() == null) {
            return Collections.emptyList();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(answer.getCont(), new TypeReference<List<AnsContDTO>>() {});
        }
    }

    /**
     * 결과분석 저장할 때 문자열 변환
     * @param ansQstDTO
     * @return
     * @throws JsonProcessingException
     */
    public static String getAnsAnalysis(List<AnsQstDTO> ansQstDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ansQstDTO);
    }

    /**
     * 결과분석 추가할 때 맵핑하는 함수
     * @param saveFile
     * @return
     * @throws JsonProcessingException
     */
    public static List<AnsQstDTO> setAnsAnalysis(String saveFile) throws JsonProcessingException{
        if(saveFile == null){
            return Collections.emptyList();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(saveFile, new TypeReference<List<AnsQstDTO>>() {});
        }
    }

    public Long getCountByaboxId(Long aboxId) {
        return ansRepository.countByAboxId(aboxId);
    }

    /**
     * Ans 첫번째 저장
     * @param ansDto
     * @return
     * @throws JsonProcessingException
     */
    @Transactional
    public Long createAns(AnsDTO ansDto) throws JsonProcessingException {
        Long curMemberId = SecurityUtil.getCurrentMemberId();
        if(curMemberId == curMemberId){
            Member member = memberRepository.getOne(curMemberId);
            Enq enq = enqRepository.getOne(ansDto.getEnqId());
            Abox defaultAbox = aboxRepository.findByMemberId(curMemberId).get(0);

            if(curMemberId == curMemberId){
                Ans answer = Ans.builder()
                        .member(member)
                        .enq(enq)
                        .abox(defaultAbox)
                        .name(ansDto.getName())
                        .cont(setAnsCont(ansDto.getAnsCont()))
                        .status(AnsStatus.SAVE)
                        .updateDate(LocalDate.now())
                        .isShow(true)
                        .build();
                ansRepository.save(answer);
                AnsDTO addResult = AnsDTO.builder()
                        .ansCont(ansDto.getAnsCont())
                        .id(answer.getId())
                        .aboxId(ansDto.getAboxId())
                        .enqId(ansDto.getEnqId())
                        .build();
                if(!addAnalysis(addResult, curMemberId)) return 0L;//테스팅 끝나면 지우기
                return answer.getId();
            }
            else return 0L;
        } else return 0L;
    }
//    public AnsRspDTO updateAns(AnsDTO ansDTO) throws JsonProcessingException {
//        Long memberId = SecurityUtil.getCurrentMemberId();
//        Ans ans = ansRepository.getOne(ansDTO.getId());
//        if(ansDTO.getMemberId() == memberId){
//            ans.toBuilder()
//                    .updateDate(LocalDate.now())
//                    .cont(setAnsCont(ansDTO.getAnsCont()))
//                    .build();
//            ansRepository.save(ans);
//            return AnsRspDTO.builder()
//                    .id(ansDTO.getId())
//                    .ansCont(ansDTO.getAnsCont()).build();
//        } else{
//            return null;
//        }
//    }

    /**
     * 저장되어 있는 Answer 조회
     * @param ansId
     * @return
     * @throws JsonProcessingException
     */
    public AnsRspDTO getAns(Long ansId) throws JsonProcessingException {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Ans> ans = ansRepository.findById(ansId);
        if(ans.get().getMember().getId() == memberId){
            AnsRspDTO ansRspDTO = AnsRspDTO.builder()
                    .id(ans.get().getId())
                    .ansCont(getAnsCont(ans.get()))
                    .build();

            return ansRspDTO;
        } else{
            return null;
        }
    }

    @Transactional
    public void updateAns(AnsUpdateDTO ansUpdateDTO) throws JsonProcessingException {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Ans answer = ansRepository.findById(ansUpdateDTO.getAnsId())
                .orElseThrow(() -> new EntityNotFoundException("Answer not found"));

        answer.toBuilder()
                .name(ansUpdateDTO.getName())
                .cont(setAnsCont(ansUpdateDTO.getAnsCont()))
                .build();

        AnsDTO addData = AnsDTO.builder()
                .id(ansUpdateDTO.getAnsId())
                .enqId(answer.getEnq().getId())
                .aboxId(answer.getAbox().getId())
                .ansCont(ansUpdateDTO.getAnsCont())
//                .memberId(memberId)
                .name(ansUpdateDTO.getName())
                .build();

        addAnalysis(addData, memberId); //테스팅 다 하면 지우기

        ansRepository.save(answer);
    }

    /**
     * 제출
     * @param reqDto
     * @return
     * @throws JsonProcessingException
     */
    @Transactional
    public AnsRspDTO submitAns(AnsDTO reqDto) throws JsonProcessingException {
        Long memberId = SecurityUtil.getCurrentMemberId();

        if (memberId == memberId) {
            Optional<Ans> ans = ansRepository.findById(reqDto.getId());
            if (ans.isPresent()) {
                Ans curAns = ans.get().toBuilder().status(AnsStatus.SUBMIT).build();
                ansRepository.save(curAns);
                if(addAnalysis(reqDto, memberId)) return null;
                return AnsRspDTO.builder().id(curAns.getId()).ansCont(reqDto.getAnsCont()).build();
            } else {
                Long ansId = createAns(reqDto);
                Ans curAns = ansRepository.getOne(ansId);
                curAns.toBuilder()
                        .status(AnsStatus.SUBMIT)
                        .build();
                ansRepository.save(curAns);
                if(!addAnalysis(reqDto, memberId)) return null;
                return AnsRspDTO.builder().id(ansId).ansCont(reqDto.getAnsCont()).build();
            }
        } else return null;
    }


    /**
     * 설문 참여함 응답지 폴더 이동 함수
     * @param ansMoveDTO
     */
    @Transactional
    public void moveAns(AnsMoveDTO ansMoveDTO, Long ansId){
        Optional<Ans> answer = ansRepository.findById(ansId);
        Abox abox = aboxRepository.getOne(ansMoveDTO.getAboxId());

        if(answer.isPresent()){
            Ans ans = answer.get().toBuilder()
                    .abox(abox)
                    .build();
            ansRepository.save(ans);
        } else{
            System.out.println("해당 id에 대한 데이터가 존재하지 않습니다.");
        }



    }


    /**
     * Ans 삭제 요청 시 동작하는 함수 정의
     * @param ansId
     */
    @Transactional
    public void AnsDeleteReq(Long ansId) {
        Optional<Ans> optionalAns = ansRepository.findById(ansId);
        // 예외처리 해야합니다.

        Ans nowAns = optionalAns.get();

        if(nowAns.getStatus() == AnsStatus.SUBMIT) {
            Ans modifiedAns = nowAns.toBuilder()
                    .isShow(false)
                    .build();
            ansRepository.save(modifiedAns);
        }
        else {
            ansRepository.delete(nowAns);
        }
    }

    /**
     * enq가 배포됬을 때 실행해야 함
     * @param enq
     * @throws JsonProcessingException
     */
    @Transactional
    public void defaultAnalysis(Enq enq) throws JsonProcessingException {
//        Enq enq = enqRepository.getOne(enqId);
        List<EnqContDTO> enqDto = EnqService.getEnqCont(enq);//enq에 저장되어 있는 Qst, Opt를 전부 불러옴
        List<AnsQstDTO> ansQstDTO = new ArrayList<>(); //저장 리스트 초기화
        for(int enqQstIdx = 0; enqQstIdx < enqDto.size();enqQstIdx++){ //질문을 전부 순회
            EnqContDTO enqQst = enqDto.get(enqQstIdx);
            List<AnsOptionDTO> optionId = new ArrayList<>(); //질문 안에 있는 옵션 저장리스트
            //질문 내 옵션 전부 순회
            for(int enqOptIdx =0; enqOptIdx < enqQst.getOptions().size(); enqOptIdx++){
                AnsOptionDTO saveOption = AnsOptionDTO.builder()
                        .optId(enqQst.getOptions().get(enqOptIdx).getOptionId())
                        .optCnt(0L)
                        .build();
                optionId.add(saveOption);
            }
            if(enqQst.getAnonymous()){ //익명일 때 초기설정
                AnsKindOfDTO saveKindof = AnsKindOfDTO.builder()
                        .all(optionId)
                        .build();
                AnsQstDTO saveQst = AnsQstDTO.builder()
                        .qstId(enqQst.getQstId())
                        .qstType(enqQst.getQstType())
                        .anonymous(enqQst.getAnonymous())
                        .qstAnsKind(saveKindof)
                        .build();
                ansQstDTO.add(saveQst);
            } else {
                AnsAgeDTO saveAge = AnsAgeDTO.builder()
                        .ten(optionId)
                        .twen(optionId)
                        .thrt(optionId)
                        .four(optionId)
                        .fiftOver(optionId)
                        .build();

                AnsGenderDTO saveGender = AnsGenderDTO.builder()
                        .man(optionId)
                        .woman(optionId)
                        .build();

                AnsKindOfDTO saveKindof = AnsKindOfDTO.builder()
                        .age(saveAge)
                        .gender(saveGender)
                        .all(optionId)
                        .build();

                AnsQstDTO saveQst = AnsQstDTO.builder()
                        .qstId(enqQst.getQstId())
                        .qstType(enqQst.getQstType())
                        .anonymous(false)
                        .qstAnsKind(saveKindof)
                        .qstAns(new ArrayList<>())
                        .build();

                ansQstDTO.add(saveQst);
            }
        }
//        System.out.println(ansQstDTO.toString());
//        System.out.println(getAnsAnalysis(ansQstDTO));
        Enq saveEnq = enq.toBuilder().enqAnalysis(getAnsAnalysis(ansQstDTO)).build();
        enqRepository.save(saveEnq);
    }

    @Transactional
    public boolean addAnalysis(AnsDTO reqDto, Long memberId) throws JsonProcessingException{
        Optional<Ans> ans = ansRepository.findById(reqDto.getId());
        if(memberId == ans.get().getMember().getId() && ans.isPresent()){ //현재 접속해 있는 아이디 대조비교
            Ans curAns = ans.get();
            Member member = memberRepository.getOne(memberId);
            Enq enq = enqRepository.getOne(curAns.getEnq().getId());

            List<AnsQstDTO> updateAnalysis = new ArrayList<>(); //덮어쓸 리스트
            List<AnsQstDTO> allAnalysis = setAnsAnalysis(enq.getEnqAnalysis()); // 저장되어 있는 리스트
            List<AnsAnlContDTO> userAns = parseData(reqDto.getAnsCont());

            GenderType gender = member.getGender();
            int userAge = calAge(member.getBirthday()); //한국나이 기준

            int idx = 0; //user qstID 돌릴 때 쓸 인덱스
            for(int analysisIdx = 0; analysisIdx < allAnalysis.size(); analysisIdx++){
                AnsQstDTO matchAnalysis = allAnalysis.get(analysisIdx); //저장되어있는 것 불러오기

                if(idx >= userAns.size()) {// user 응답이 끝나면 남은걸 전부 저장하는 로직
                    updateAnalysis.add(matchAnalysis);
                    continue;
                }

                AnsAnlContDTO userAnsCont = userAns.get(idx);//유저가 응답한 리스트 갖고오기
                List<String> userOpt = userAnsCont.getOptionId();//옵션도 가져와
                String userQstId = userAnsCont.getQstId();//질문 아이디도 갖고와

                if(userOpt.isEmpty()){
                    updateAnalysis.add(matchAnalysis);
                    continue;
                }

                if(matchAnalysis.getQstId().equals(userQstId)) { //질문 아이디 대조비교
                     //주관식 일 때 또는 익명일 때
                     if(matchAnalysis.getQstType().equals("서술형 질문")){
                        List<String> updateAns = matchAnalysis.getQstAns();
                        updateAns.add(userAnsCont.getAnswerText());
                        matchAnalysis = matchAnalysis.toBuilder().qstAns(updateAns).build();

                     }
                     else if(matchAnalysis.getAnonymous()){ //익명성 질문인지 확인
                         AnsKindOfDTO currentKindOf = matchAnalysis.getQstAnsKind();
                         List<AnsOptionDTO> updateAllOption = new ArrayList<>(); //저장할 옵션
                         List<AnsOptionDTO> currentAllOption = currentKindOf.getAll();

                         int userAnsOptIdx = 0; //자신이 갖고있는 옵션 정보 인덱스
                         for(int optIdx = 0; optIdx < currentAllOption.size(); optIdx++){ //All 저장
                             AnsOptionDTO ansOptionDTO = currentAllOption.get(optIdx);

                             if(userAnsOptIdx >= userOpt.size()){ //응답이 끝나면 나머지 저장하고 끝
                                 updateAllOption.add(ansOptionDTO);
                                 continue;
                             }

                             //옵션 아이디 대조비교 및 결과반영
                             if(ansOptionDTO.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                 Long saveCnt = ansOptionDTO.getOptCnt()+1L;
                                 AnsOptionDTO saveOpt = ansOptionDTO.toBuilder()
                                         .optCnt(saveCnt)
                                         .build();
                                 updateAllOption.add(saveOpt);
                                 userAnsOptIdx++;
                                 continue;
                             }
                             updateAllOption.add(ansOptionDTO);
                         }
                         AnsKindOfDTO saveKindof = currentKindOf
                                 .toBuilder()
                                 .all(updateAllOption)
                                 .build();

                         matchAnalysis
                                 .toBuilder()
                                 .qstAnsKind(saveKindof)
                                 .build();

                     } else { //종류별로 전부 저장
                         AnsKindOfDTO currentKindOf = matchAnalysis.getQstAnsKind();
                         List<AnsOptionDTO> updateAllOpt = new ArrayList<>(); //all속성 저장리스트
                         List<AnsOptionDTO> updateGenderOpt = new ArrayList<>(); //gedner속성 저장리스트
                         List<AnsOptionDTO> updateAgeOpt = new ArrayList<>(); //age속성 저장리스트

                         List<AnsOptionDTO> currentAllOption = currentKindOf.getAll(); //3가지 종류중 ALL 가져오기
                         int userAnsOptIdx = 0; //자신이 갖고있는 옵션 정보 인덱스
                         for(int optIdx = 0; optIdx < currentAllOption.size(); optIdx++){ //All 저장
                             AnsOptionDTO ansOptionDTO = currentAllOption.get(optIdx);

                             if(userAnsOptIdx >= userOpt.size()) {//유저의 응답이 끝나면 반복문 종료
                                 updateAllOpt.add(ansOptionDTO);
                                 continue;
                             }

                             if(ansOptionDTO.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                 Long saveCnt = ansOptionDTO.getOptCnt()+1L;
                                 AnsOptionDTO updateDto = ansOptionDTO.toBuilder()
                                         .optCnt(saveCnt)
                                         .build();
                                 updateAllOpt.add(updateDto);
                                 userAnsOptIdx++;
                                 continue;
                             }
                             updateAllOpt.add(ansOptionDTO);
                         }
                         //All저장
                         currentKindOf = currentKindOf.toBuilder().all(updateAllOpt).build();

                         //gender Logic
                         userAnsOptIdx = 0;
                         if(gender == GenderType.MAN){
                             List<AnsOptionDTO> manOpt = currentKindOf.getGender().getMan();
                             for(AnsOptionDTO index : manOpt){
                                 if(userAnsOptIdx >= userOpt.size()){
                                     updateGenderOpt.add(index);
                                     continue;
                                 }
                                 if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                     Long saveCnt = index.getOptCnt()+1L;
                                     AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                     updateGenderOpt.add(updateDto);
                                     userAnsOptIdx++;
                                     continue;
                                 }
                                 updateGenderOpt.add(index);
                             }

                             AnsGenderDTO saveGender = currentKindOf.getGender()
                                     .toBuilder()
                                     .man(updateGenderOpt)
                                     .build();
                             currentKindOf = currentKindOf
                                     .toBuilder()
                                     .gender(saveGender)
                                     .build();
                         } else if(gender == GenderType.WOMAN){
                             List<AnsOptionDTO> womanOpt = currentKindOf.getGender().getWoman();
                             for(AnsOptionDTO index : womanOpt){
                                 if(userAnsOptIdx >= userOpt.size()){
                                     updateGenderOpt.add(index);
                                     continue;
                                 }
                                 if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                     Long saveCnt = index.getOptCnt()+1L;
                                     AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                     updateGenderOpt.add(updateDto);
                                     userAnsOptIdx++;
                                     continue;
                                 }
                                 updateGenderOpt.add(index);
                             }
                             AnsGenderDTO saveGender = currentKindOf.getGender()
                                     .toBuilder()
                                     .woman(updateGenderOpt)
                                     .build();

                             currentKindOf = currentKindOf
                                     .toBuilder()
                                     .gender(saveGender)
                                     .build();
                         }

                         //Age Logic
                         userAnsOptIdx = 0;
                         AnsAgeDTO curAge = currentKindOf.getAge(); //현재 나이관련 설문결과를 불러옴
                         //10대
                        if(userAge < 20){// 10대 이하
                            //10대 설문 결과를 불러옴
                            List<AnsOptionDTO> tenOpt = curAge.getTen();
                            //옵션 질문 ID를 확인하면서 통계에 +1을 해줌
                            for(AnsOptionDTO index : tenOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }
                            AnsAgeDTO saveAge = curAge
                                    .toBuilder()
                                    .ten(updateAgeOpt)
                                    .build();
                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        } else if(userAge < 30) { //20대
                            List<AnsOptionDTO> twenOpt = curAge.getTwen();
                            for(AnsOptionDTO index : twenOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }
                            AnsAgeDTO saveAge = curAge
                                    .toBuilder()
                                    .twen(updateAgeOpt)
                                    .build();
                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        } else if(userAge < 40) {//30대
                            List<AnsOptionDTO> thrtOpt = curAge.getThrt();
                            for(AnsOptionDTO index : thrtOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }
                            AnsAgeDTO saveAge = curAge
                                    .toBuilder()
                                    .thrt(updateAgeOpt)
                                    .build();

                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        } else if(userAge < 50) {//40대
                            List<AnsOptionDTO> fourOpt = curAge.getFour();
                            for(AnsOptionDTO index : fourOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }
                            AnsAgeDTO saveAge = curAge
                                    .toBuilder()
                                    .four(updateAgeOpt)
                                    .build();

                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        } else { //50대 이상
                            List<AnsOptionDTO> fiftOverOpt = curAge.getFiftOver();
                            for(AnsOptionDTO index : fiftOverOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }

                            AnsAgeDTO saveAge = curAge.toBuilder()
                                    .fiftOver(updateAgeOpt)
                                    .build();

                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        }

                        //모든 로직 저장
                         matchAnalysis = matchAnalysis
                                 .toBuilder()
                                 .qstAnsKind(currentKindOf)
                                 .build();

                     }
                     idx++;
                     updateAnalysis.add(matchAnalysis);
                     continue;
                 }
                 updateAnalysis.add(matchAnalysis);
            }
            System.out.println(getAnsAnalysis(updateAnalysis));
            enq = enq.toBuilder().enqAnalysis(getAnsAnalysis(updateAnalysis)).build();
            enqRepository.save(enq);
            return true;
        } else {
            return false;
        }
    }

    public AnsAnalysisDTO getAnalysis(Long enqID) throws JsonProcessingException{
        Optional<Enq> enq = enqRepository.findById(enqID);
        if(enq.isPresent()){
            Enq curEnq = enq.get();
            AnsAnalysisDTO rspDTO = AnsAnalysisDTO.builder()
                    .ansQstDto(setAnsAnalysis(curEnq.getEnqAnalysis()))
                    .build();
            return rspDTO;
        } else{
            return null;
        }
    }


    public void save(Ans ans) {
        ansRepository.save(ans);
    }

    public int calAge(String birthday){

        int birthYear =Integer.parseInt(birthday.substring(0, 4));
        int nowDateYear = Integer.parseInt(LocalDate.now().toString().substring(0, 4));
        int currentAge = nowDateYear - birthYear + 1;

        return currentAge;
    }

    public List<AnsAnlContDTO> parseData(List<AnsContDTO> ansContDTOs){
        List<AnsAnlContDTO> anlData = new ArrayList<>();
        AnsContDTO init = ansContDTOs.get(0);
        List<String> initOpt = new ArrayList<>();
        initOpt.add(init.getOptionId());
        AnsAnlContDTO initData =AnsAnlContDTO.builder()
                .qstId(init.getQstId())
                .optionId(initOpt)
                .answerText(init.getAnswerText())
                .build();
        anlData.add(initData);
        int anlDataIdx=0;
        for(int index = 1; index < ansContDTOs.size(); index++){
            AnsContDTO ansContDTO = ansContDTOs.get(index);
            if(anlData.get(anlDataIdx).getQstId().equals(ansContDTO.getQstId())){
                List<String> updateOpt = anlData.get(anlDataIdx).getOptionId();
                updateOpt.add(ansContDTO.getOptionId());
                AnsAnlContDTO updateCont = anlData.get(anlDataIdx).toBuilder()
                        .optionId(updateOpt)
                        .build();
                anlData.set(anlDataIdx, updateCont);
            } else {
                List<String> addOpt = new ArrayList<>();
                addOpt.add(ansContDTO.getOptionId());
                AnsAnlContDTO addAnlCont = AnsAnlContDTO.builder()
                        .qstId(ansContDTO.getQstId())
                        .optionId(addOpt)
                        .answerText(ansContDTO.getAnswerText())
                        .build();
                anlData.add(addAnlCont);
            }
        }
        return anlData;
    }

//    private boolean containsOptId(List<AnsOptionDTO> options, String optId) {
//        for (AnsOptionDTO option : options) {
//            if (option.getOptId().equals(optId)) {
//                return true;
//            }
//        }
//        return false;
//    }
//    public List<AnsContDTO> parsingContData(List<AnsReqContDTO> reqDTO){
//        List<AnsContDTO> saveCont = new ArrayList<>();
//        AnsReqContDTO initcontDTO = reqDTO.get(0);
//        if(reqDTO.size() == 1) {
//            if(initcontDTO.getQstType().equals("주관식")){
//                AnsContDTO saveData = AnsContDTO.builder()
//                        .qstId(initcontDTO.getQstId())
//                        .qstType(initcontDTO.getQstType())
//                        .qstAns(initcontDTO.getAnswerText())
//                        .build();
//                saveCont.add(saveData);
//            } else{
//                List<String> ansOptDTO = new ArrayList<>();
//                ansOptDTO.add(initcontDTO.getOptionId());
//                AnsContDTO saveData = AnsContDTO.builder()
//                        .qstId(initcontDTO.getQstId())
//                        .qstType(initcontDTO.getQstType())
//                        .optId(ansOptDTO)
//                        .build();
//                saveCont.add(saveData);
//            }
//            return saveCont;
//        }
//
//        if(initcontDTO.getQstType().equals("주관식")){
//            AnsContDTO saveInit = AnsContDTO.builder()
//                    .qstId(initcontDTO.getQstId())
//                    .qstType(initcontDTO.getQstType())
//                    .qstAns(initcontDTO.getAnswerText())
//                    .build();
//            saveCont.add(saveInit);
//        } else{
//            List<String> initOpt =  new ArrayList<>();
//            initOpt.add(initcontDTO.getOptionId());
//            AnsContDTO saveInit = AnsContDTO.builder()
//                    .qstId(initcontDTO.getQstId())
//                    .qstType(initcontDTO.getQstType())
//                    .optId(initOpt)
//                    .build();
//            saveCont.add(saveInit);
//        }
//
//        int idx = 0;
//        for(int index = 1; index < reqDTO.size(); index++){
//            AnsReqContDTO leftData = reqDTO.get(index-1);
//            AnsReqContDTO rightData = reqDTO.get(index);
//            if(leftData.getQstId().equals(rightData.getQstId())){
//                AnsContDTO updateCont = saveCont.get(idx);
//                List<String> updateOpt = updateCont.getOptId();
//                updateOpt.add(rightData.getOptionId());
//                updateCont.toBuilder().optId(updateOpt).build();
//                saveCont.set(idx, updateCont);
//            } else {
//                if(rightData.getQstType().equals("주관식")){
//                    AnsContDTO newCont = AnsContDTO.builder()
//                            .qstId(rightData.getQstId())
//                            .qstType(rightData.getQstType())
//                            .qstAns(rightData.getAnswerText())
//                            .build();
//                    saveCont.add(newCont);
//                } else{
//                    List<String> option = new ArrayList<>();
//                    option.add(rightData.getOptionId());
//                    AnsContDTO newCont = AnsContDTO.builder()
//                            .qstId(rightData.getQstId())
//                            .qstType(rightData.getQstType())
//                            .optId(option)
//                            .build();
//                    saveCont.add(newCont);
//                }
//                idx++;
//            }
//
//        }
//        return saveCont;
//    }
}
