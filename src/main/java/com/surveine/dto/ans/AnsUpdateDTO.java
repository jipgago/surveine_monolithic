package com.surveine.dto.ans;


import com.surveine.dto.ans.anscontents.AnsContDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@ToString
public class AnsUpdateDTO {

    private Long ansId;
    @NotNull
    private final String name;
    @NotNull
    private final List<AnsContDTO> ansCont;
    @Builder(toBuilder = true)
    public AnsUpdateDTO(Long ansId, String name, List<AnsContDTO> ansCont) {
        this.ansId = ansId;
        this.name =  name;
        this.ansCont = ansCont;
    }
}
