package com.surveine.dto.enq;

import com.surveine.dto.enq.enqcontents.EnqContDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Enq 저장하고 다시 수정할 수 있게
 * 응답주는 DTO
 */
@Getter
@NoArgsConstructor
public class EnqCreateRspDTO {
    private Long enqId;
    private Long memberId;
    private Long cboxId;
    private String enqName;
    private String enqTitle;
    private List<EnqContDTO> enqCont;
    private String enqNanoId;

    @Builder
    public EnqCreateRspDTO(Long enqId, Long memberId, Long cboxId, String enqTitle, String enqName, List<EnqContDTO> cont, String enqNanoId) {
        this.enqId = enqId;
        this.memberId = memberId;
        this.cboxId = cboxId;
        this.enqTitle = enqTitle;
        this.enqName = enqName;
        this.enqCont = cont;
        this.enqNanoId = enqNanoId;
    }
}
