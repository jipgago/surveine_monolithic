package com.surveine.dto.cbox;

import com.surveine.domain.Cbox;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CboxDTO {
    private Long cboxId;
    private String cboxName;
    private Long enqCnt;

    @Builder
    public CboxDTO(Cbox cbox, Long enqCnt) {
        this.cboxId = cbox.getId();
        this.cboxName = cbox.getName();
        this.enqCnt = enqCnt;
    }
}
