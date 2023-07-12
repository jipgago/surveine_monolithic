package com.surveine.dto.ans.analysis;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AnsAnlContDTO {
    private String qstId;
    private List<String> optionId;
    private String answerText;
    @Builder(toBuilder = true)
    public AnsAnlContDTO(String qstId, List<String> optionId, String answerText) {
        this.qstId = qstId;
        this.optionId = optionId;
        this.answerText = answerText;
    }
}
