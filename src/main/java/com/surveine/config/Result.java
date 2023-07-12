package com.surveine.config;

import lombok.*;
//임혜균 깃 잘 되는지 체크
@Getter
public class Result {
    private Boolean isSuccess;
    private String message;
    private Object result;

    @Builder
    public Result(Boolean isSuccess, String message, Object result) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.result = result;
    }
}
