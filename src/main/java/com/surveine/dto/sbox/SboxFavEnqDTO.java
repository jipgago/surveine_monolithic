package com.surveine.dto.sbox;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 좋아요 한 Enq 제공 DTO
 * SboxFavEnqList 하위 DTO
 */
@Getter
public class SboxFavEnqDTO {
    private Long enqId;
    private Long favCount;
    private String enqName;
    private boolean isFav;

    @Builder
    public SboxFavEnqDTO(Long enqId, Long favCount, String enqName, boolean isFav) {
        this.enqId = enqId;
        this.favCount = favCount;
        this.enqName = enqName;
        this.isFav = isFav;
    }
}
