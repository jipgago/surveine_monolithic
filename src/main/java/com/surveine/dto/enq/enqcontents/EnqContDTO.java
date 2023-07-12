package com.surveine.dto.enq.enqcontents;
import lombok.*;

import java.util.List;

/**
 * 컨텐츠 저장 DTO
 * EnqCreate 및 EnqUpdate 하위 DTO
 */
@Getter
@NoArgsConstructor
public class EnqContDTO {
    private String qstId;
    private String qstType;
    private String qstTitle;
    //    private String qstImg;
    private Boolean anonymous;
    private Boolean essential;
    private Boolean branch;
    private String branchQst;
    private String branchOpt;
    private List<EnqOptionContDTO> options;

    @Builder
    public EnqContDTO(String qstId, String qstType, String qstTitle, Boolean anonymous, Boolean essential, Boolean branch, List<EnqOptionContDTO> options, String branchQst, String branchOpt) {
        this.qstId = qstId;
        this.qstType = qstType;
        this.qstTitle = qstTitle;
//        this.qstImg = qstImg;
        this.anonymous = anonymous;
        this.essential = essential;
        this.branch = branch;
        this.branchQst = branchQst;
        this.branchOpt = branchOpt;
        this.options = options;
    }
}
