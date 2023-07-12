package com.surveine.dto.abox;

import com.surveine.domain.Abox;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AboxDTO {
    private Long aboxId;
    private String aboxName;
    private Long ansCnt;

    @Builder
    public AboxDTO(Abox abox, Long ansCnt) {
        this.aboxId = abox.getId();
        this.aboxName = abox.getName();
        this.ansCnt = ansCnt;
    }
}
