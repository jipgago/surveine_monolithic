package com.surveine.dto.abox;

import com.surveine.domain.Cbox;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AboxNameDTO {
    private String aboxName;

    public AboxNameDTO() {
    }

    public void setAboxName(String aboxName) {
        this.aboxName = aboxName;
    }

    @Builder
    public AboxNameDTO(Cbox cbox) {
        this.aboxName = cbox.getName();
    }
}
