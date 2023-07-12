package com.surveine.dto.enq;
import com.surveine.dto.enq.enqcontents.EnqContDTO;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Enq 업데이트 해주는 DTO
 */
@Getter
public class EnqUpdateDTO {
    private Long enqId;
    @NotNull
    private final String enqName;
    @NotNull
    private final List<EnqContDTO> enqCont;
    @Builder(toBuilder = true)
    public EnqUpdateDTO(Long enqId, String enqName, List<EnqContDTO> enqCont) {
        this.enqId = enqId;
        this.enqName = enqName;
        this.enqCont = enqCont;
    }
}
