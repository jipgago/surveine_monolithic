package com.surveine.dto.cbox;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CboxSboxDTO {
    private Long cboxId;
    private String cboxName;

    @Builder
    public CboxSboxDTO(Long cboxId, String cboxName) {
        this.cboxId = cboxId;
        this.cboxName = cboxName;
    }
}
