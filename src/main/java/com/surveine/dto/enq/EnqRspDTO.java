package com.surveine.dto.enq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.domain.Cbox;
import com.surveine.domain.Enq;
import com.surveine.dto.enq.enqcontents.EnqContDTO;
import com.surveine.enums.DistType;
import com.surveine.enums.EnqStatus;
import com.surveine.service.EnqService;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * Enq 조회 DTO
 */
@Getter
public class EnqRspDTO {
    private Long id;
    private Long cbox_id;
    private String name;
    private List<EnqContDTO> cont;
    private Boolean isShared;
    private EnqStatus enqStatus;
    private DistType distType;
    private LocalDate updateDate;
    //TODO: Enq 컬럼이랑 같아야 되는지 확인

    @Builder
    public EnqRspDTO(Enq enq) throws JsonProcessingException {
        this.id = enq.getId();
        this.cbox_id = enq.getCbox().getId();
        this.name = enq.getName();
        this.cont = EnqService.getEnqCont(enq);
        this.isShared = enq.getIsShared();
        this.enqStatus = enq.getEnqStatus();
        this.distType = enq.getDistType();
        this.updateDate = enq.getUpdateDate();
    }

}
