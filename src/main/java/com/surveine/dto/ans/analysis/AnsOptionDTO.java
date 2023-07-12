package com.surveine.dto.ans.analysis;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class AnsOptionDTO {
    private String optId;
    private Long optCnt;

    @Builder(toBuilder = true)
    public AnsOptionDTO(String optId, Long optCnt) {
        this.optId = optId;
        this.optCnt = optCnt;
    }
}
