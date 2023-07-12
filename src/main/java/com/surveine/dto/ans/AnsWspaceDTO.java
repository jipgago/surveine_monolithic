package com.surveine.dto.ans;

import com.surveine.domain.*;
import com.surveine.enums.AnsStatus;
import com.surveine.enums.DistType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AnsWspaceDTO {
    private Long ansId;

    private String enqName;

    private AnsStatus ansStatus;

    private Boolean isShow;

    private DistType distType;

    private LocalDate updateDate;

    @Builder
    public AnsWspaceDTO(Ans ans) {
        this.ansId = ans.getId();
        this.enqName = ans.getEnq().getName();
        this.ansStatus = ans.getStatus();
        this.isShow = ans.getIsShow();
        this.distType = ans.getEnq().getDistType();
        this.updateDate = ans.getUpdateDate();
    }
}
