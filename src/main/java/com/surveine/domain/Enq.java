package com.surveine.domain;

import com.surveine.enums.DistType;
import com.surveine.enums.EnqStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance
@ToString
@SuperBuilder(toBuilder = true)
@Table(name = "enq")
public class Enq {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enq_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name  = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cbox_id")
    private Cbox cbox;

    @Column(name = "enq_name")
    private String name;

    @Column(name = "enq_title")
    private String title;

    @Column(name = "enq_cont", columnDefinition = "TEXT")
    private String cont;

    @Column(name = "is_shared")
    private Boolean isShared;

    @Column(name = "enq_status")
    @Enumerated(EnumType.STRING)
    private EnqStatus enqStatus;

    @Column(name = "dist_type")
    @Enumerated(EnumType.STRING)
    private DistType distType;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "fav_count")
    private Long favCount;

    @Column(name = "enq_analysis", columnDefinition = "TEXT")
    private String enqAnalysis;

    @Column(name = "enq_report")
    private Long enqReport;


    /**
     * 배포관련 컬럼
     */
    //link, gps
    @Column(name = "quota")
    private Integer quota;

    @Column(name = "start_date")
    private LocalDateTime startDateTime;

    @Column(name = "end_date")
    private LocalDateTime endDateTime;

    @Column(name = "ansed_cnt")
    private Integer ansedCnt;

    //link
    @Column(name = "dist_link")
    private String distLink;

    //gps
    @Column(name = "my_location")
    private Point my_location;

    @Column(name = "dist_range")
    private Integer distRange;



    public Enq(Long id, Member member, Cbox cbox, String name, String title, String cont, Boolean isShared, EnqStatus enqStatus, DistType distType, LocalDate updateDate, Long favCount, Integer quota, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer ansedCnt, String distLink, Point my_location, Integer distRange, String enqAnalysis, Long enqReport) {
        this.id = id;
        this.member = member;
        this.cbox = cbox;
        this.name = name;
        this.title = title;
        this.cont = cont;
        this.isShared = isShared;
        this.enqStatus = enqStatus;
        this.distType = distType;
        this.updateDate = updateDate;
        this.favCount = favCount;
        this.quota = quota;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.ansedCnt = ansedCnt;
        this.distLink = distLink;
        this.my_location = my_location;
        this.distRange = distRange;
        this.enqAnalysis = enqAnalysis;
        this.enqReport = enqReport;
    }

}
