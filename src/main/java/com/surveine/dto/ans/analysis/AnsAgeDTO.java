package com.surveine.dto.ans.analysis;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class AnsAgeDTO {
    private List<AnsOptionDTO> ten;
    private List<AnsOptionDTO> twen;
    private List<AnsOptionDTO> thrt;
    private List<AnsOptionDTO> four;
    private List<AnsOptionDTO> fiftOver;

    @Builder(toBuilder = true)
    public AnsAgeDTO(List<AnsOptionDTO> ten, List<AnsOptionDTO> twen, List<AnsOptionDTO> thrt, List<AnsOptionDTO> four, List<AnsOptionDTO> fiftOver) {
        this.ten = ten;
        this.twen = twen;
        this.thrt = thrt;
        this.four = four;
        this.fiftOver = fiftOver;
    }
}
