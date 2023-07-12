package com.surveine.dto.enq;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@SuperBuilder(toBuilder = true)
public class EnqLinkDistDTO {
    private Long enqId;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private Integer quota;
    private String distLink;


    public EnqLinkDistDTO(Long enqId, String startDate, String startTime, String endDate, String endTime, Integer quota, String distLink) {
        this.enqId = enqId;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.quota = quota;
        this.distLink = distLink;
    }
}


//package com.surveine.dto.enq;
//
//import lombok.Getter;
//import lombok.experimental.SuperBuilder;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Date;
//
//@Getter
//@SuperBuilder(toBuilder = true)
//public class EnqLinkDistDTO {
//    private Long enqId;
//    private LocalDate startDate;
//    private LocalTime startTime;
//    private LocalDate endDate;
//    private LocalTime endTime;
//    private Integer quota;
//    private String distLink;
//
//
//    public EnqLinkDistDTO(Long enqId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, Integer quota, String distLink) {
//        this.enqId = enqId;
//        this.startDate = startDate;
//        this.startTime = startTime;
//        this.endDate = endDate;
//        this.endTime = endTime;
//        this.quota = quota;
//        this.distLink = distLink;
//    }
//}
