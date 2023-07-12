package com.surveine.dto.sbox;

import lombok.Builder;
import lombok.Getter;

/**
 * 폴더 변경 때 필요한 DTO
 */
@Getter
public class SboxMoveMyCboxDTO {
    private Long cboxId;
    private Long enqId;

    @Builder
    public SboxMoveMyCboxDTO(Long cboxId, Long enqId) {
        this.cboxId = cboxId;
        this.enqId = enqId;
    }
}
