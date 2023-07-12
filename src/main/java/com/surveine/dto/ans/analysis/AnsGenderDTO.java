package com.surveine.dto.ans.analysis;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class AnsGenderDTO {
    private List<AnsOptionDTO> man;
    private List<AnsOptionDTO> woman;
    @Builder(toBuilder = true)
    public AnsGenderDTO(List<AnsOptionDTO> man, List<AnsOptionDTO> woman) {
        this.man = man;
        this.woman = woman;
        //롤백
    }
}
