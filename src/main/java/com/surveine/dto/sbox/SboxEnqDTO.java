package com.surveine.dto.sbox;

import lombok.Builder;
import lombok.Getter;

/**
 * SboxEnqList 하위 DTO
 * 커피콩 MappingDTO
 */
@Getter
public class SboxEnqDTO {
    private Long enqId;
    private Long favCount;
    private String enqName;
    private boolean isFav;

    @Builder
    public SboxEnqDTO(Long enqId, Long favCount, String enqName, boolean isFav) {
        this.enqId = enqId;
        this.favCount = favCount;
        this.enqName = enqName;
        this.isFav = isFav;
    }
}
