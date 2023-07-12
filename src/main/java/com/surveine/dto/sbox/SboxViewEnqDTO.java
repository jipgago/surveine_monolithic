package com.surveine.dto.sbox;

import com.surveine.dto.enq.enqcontents.EnqContDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 개별 Enq 조회 DTO
 */
@Getter
public class SboxViewEnqDTO {
    private String enqName;
    private String enqTitle;
    private List<EnqContDTO> enqCont;

    @Builder
    public SboxViewEnqDTO(String enqName, String enqTitle, List<EnqContDTO> enqCont) {
        this.enqName = enqName;
        this.enqTitle = enqTitle;
        this.enqCont = enqCont;
    }
}
