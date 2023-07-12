package com.surveine.dto.ans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.domain.Enq;
import com.surveine.dto.enq.enqcontents.EnqContDTO;
import com.surveine.enums.DistType;
import com.surveine.enums.EnqStatus;
import com.surveine.service.EnqService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Getter
@NoArgsConstructor
public class AnsUrlDTO {
    private Long id;
    private Long cbox_id;
    private String name;
    private List<EnqContDTO> cont;
    private Boolean isShared;
    private EnqStatus enqStatus;
    private DistType distType;
    private LocalDate updateDate;

    private Long member_id;


    @Builder
    public AnsUrlDTO(Enq enq, Long member_id) throws JsonProcessingException {
        this.id = enq.getId();
        this.cbox_id = enq.getCbox().getId();
        this.name = enq.getName();
        this.cont = EnqService.getEnqCont(enq);
        this.isShared = enq.getIsShared();
        this.enqStatus = enq.getEnqStatus();
        this.distType = enq.getDistType();
        this.updateDate = enq.getUpdateDate();
        this.member_id = member_id;
    }
}



