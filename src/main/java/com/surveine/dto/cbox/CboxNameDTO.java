package com.surveine.dto.cbox;

import com.surveine.domain.Cbox;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CboxNameDTO {
    private String cboxName;

    public CboxNameDTO() {
    }

    public void setCboxName(String cboxName) {
        this.cboxName = cboxName;
    }

    @Builder
    public CboxNameDTO(Cbox cbox) {
        this.cboxName = cbox.getName();
    }
}

