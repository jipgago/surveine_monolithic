package com.surveine.dto.enq;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@SuperBuilder(toBuilder = true)
public class EnqGPSDistDTO {
    private Long enqId;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;

    private Integer quota;
    private Integer x;
    private Integer y;
    private Integer distRange;

    public EnqGPSDistDTO(Long enqId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, Integer quota, Integer x, Integer y, Integer distRange) {
        this.enqId = enqId;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.quota = quota;
        this.x = x;
        this.y = y;
        this.distRange = distRange;
    }

}
