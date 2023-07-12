package com.surveine.dto.enq;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class EnqStatusDTO {
    @NotNull
    Long enqId;
    @NotNull
    String enqStatus;

    @Builder(toBuilder = true)
    public EnqStatusDTO(Long enqId, String enqStatus) {
        this.enqId = enqId;
        this.enqStatus = enqStatus;
    }
}
