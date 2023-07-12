package com.surveine.dto.ans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AnsMoveDTO {
    @NotNull
    private Long aboxId;

    @Builder
    public AnsMoveDTO(Long ansId, Long aboxId){
        this.aboxId = aboxId;
    }
}
