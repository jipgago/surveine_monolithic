package com.surveine.dto.sbox;

import com.surveine.dto.cbox.CboxSboxDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 샌드박스 공유 Enq List 제공 DTO
 */
@Getter
public class SboxEnqListDTO {
    private List<CboxSboxDTO> cbList;
    private List<SboxEnqDTO> sandboxCBList;

    @Builder
    public SboxEnqListDTO(List<CboxSboxDTO> cbList, List<SboxEnqDTO> sandboxCBList) {
        this.cbList = cbList;
        this.sandboxCBList = sandboxCBList;
    }
}
