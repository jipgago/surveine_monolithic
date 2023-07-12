package com.surveine.dto.enq;

import com.surveine.domain.Enq;
import com.surveine.enums.DistType;
import com.surveine.enums.EnqStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EnqWSpaceDTO {
    private Long enqId;

    private String enqName;

    private EnqStatus enqStatus;

    private DistType distType;

    private Boolean isShared;

    private LocalDate updateDate;

    @Builder
    public EnqWSpaceDTO(Enq enq) {
        this.enqId = enq.getId();
        this.enqName = enq.getName();
        this.enqStatus = enq.getEnqStatus();
        this.isShared = enq.getIsShared();
        this.distType = enq.getDistType();
        this.updateDate = enq.getUpdateDate();
    }
}
