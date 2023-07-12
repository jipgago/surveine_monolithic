package com.surveine.dto.ans.analysis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class AnsQstDTO {

    private String qstId;

    private String qstType;

    private Boolean anonymous;

    private AnsKindOfDTO qstAnsKind;

    private List<String> qstAns;
    @Builder(toBuilder = true)
    public AnsQstDTO(String qstId, String qstType, Boolean anonymous, AnsKindOfDTO qstAnsKind, List<String> qstAns) {
        this.qstId = qstId;
        this.qstType = qstType;
        this.anonymous = anonymous;
        this.qstAnsKind = qstAnsKind;
        this.qstAns = qstAns;
    }
}
