package com.surveine.dto.ans.analysis;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Getter
@ToString
@NoArgsConstructor
public class AnsKindOfDTO {
    private AnsGenderDTO gender;
    private AnsAgeDTO age;
    private List<AnsOptionDTO> all;

    @Builder(toBuilder = true)
    public AnsKindOfDTO(AnsGenderDTO gender, AnsAgeDTO age, List<AnsOptionDTO> all) {
        this.gender = gender;
        this.age = age;
        this.all = all;
    }
}
