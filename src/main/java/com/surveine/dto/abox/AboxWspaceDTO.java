package com.surveine.dto.abox;

import com.surveine.domain.Abox;
import com.surveine.dto.ans.AnsWspaceDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AboxWspaceDTO {
    private Long aboxId;

    private String aboxName;

    private List<AnsWspaceDTO> ansList;

    @Builder
    public AboxWspaceDTO(Abox abox, List<AnsWspaceDTO> ansList) {
        this.aboxId = abox.getId();
        this.aboxName = abox.getName();
        this.ansList = ansList;
    }
}
