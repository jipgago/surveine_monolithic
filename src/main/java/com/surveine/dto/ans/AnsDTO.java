package com.surveine.dto.ans;

import com.surveine.dto.ans.anscontents.AnsContDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class AnsDTO {
    private Long id;

    private String name;

    private Long enqId;

//    private Long memberId;

    private Long aboxId;

    // 질문 응답 내용 추가
    private List<AnsContDTO> ansCont;

    @Builder(toBuilder = true)
    public AnsDTO(Long id, Long enqId, Long aboxId, List<AnsContDTO> ansCont, String name) {
        this.id = id;
        this.enqId = enqId;
        this.aboxId = aboxId;
        this.name = name;
//        this.memberId = memberId;
        this.ansCont = ansCont;
    }
}
