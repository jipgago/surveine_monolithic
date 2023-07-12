package com.surveine.dto.enq;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 폴더변경 DTO
 */
@Getter
@NoArgsConstructor
public class EnqMoveDTO {

    @NotNull
    private Long cboxId;

    @Builder(toBuilder = true)
    public EnqMoveDTO(Long cboxId){
        this.cboxId = cboxId;
    }
}
