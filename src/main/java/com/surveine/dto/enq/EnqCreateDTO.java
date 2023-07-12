package com.surveine.dto.enq;

import com.surveine.dto.enq.enqcontents.EnqContDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Save DTO
 */
@Getter
public class EnqCreateDTO {
    private Long cboxId;
    private String enqName;
    private String enqTitle;
    private List<EnqContDTO> enqCont;
    //private String enqNanoId;

    @Builder
    public EnqCreateDTO(Long cboxId, String enqTitle, String enqName, List<EnqContDTO> cont) {
        this.cboxId = cboxId;
        this.enqTitle = enqTitle;
        this.enqName = enqName;
        this.enqCont = cont;
        //this.enqNanoId = enqNanoId;
    }
}
