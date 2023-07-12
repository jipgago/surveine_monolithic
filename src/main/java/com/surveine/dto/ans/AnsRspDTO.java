package com.surveine.dto.ans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.surveine.dto.ans.anscontents.AnsContDTO;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AnsRspDTO {
    private Long id;
    private List<AnsContDTO> ansCont;

    @Builder
    public AnsRspDTO(Long id, List<AnsContDTO> ansCont) {
        this.id = id;
        this.ansCont = ansCont;
    }
}
