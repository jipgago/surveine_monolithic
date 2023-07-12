package com.surveine.dto.cbox;

import com.surveine.domain.Cbox;
import com.surveine.dto.enq.EnqWSpaceDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CboxWSpaceDTO {
    private Long cboxId;
    private String cboxName;
    private List<EnqWSpaceDTO> enqList;

    @Builder
    public CboxWSpaceDTO(Cbox cbox, List<EnqWSpaceDTO> enqList) {
        this.cboxId = cbox.getId();
        this.cboxName = cbox.getName();
        this.enqList = enqList;
    }
}
