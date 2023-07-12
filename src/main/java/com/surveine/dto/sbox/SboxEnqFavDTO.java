package com.surveine.dto.sbox;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 탬플릿 좋아요 및 취소 DTO
 */
@NoArgsConstructor
@Getter
public class SboxEnqFavDTO {

    private Long enqId;

    @Builder
    public SboxEnqFavDTO(Long enqId) {
        this.enqId = enqId;
    }
}
