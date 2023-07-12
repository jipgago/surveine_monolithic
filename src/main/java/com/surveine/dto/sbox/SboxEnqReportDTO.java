package com.surveine.dto.sbox;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 신고 DTO
 */
@Getter
@NoArgsConstructor
public class SboxEnqReportDTO {
    @NotNull
    private Long enqId;
    @Builder
    public SboxEnqReportDTO(Long enqId) {
        this.enqId = enqId;
    }
}
