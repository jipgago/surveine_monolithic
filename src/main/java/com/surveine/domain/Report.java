package com.surveine.domain;

import lombok.*;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "report")
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name  = "member_id")
    private Member memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name  = "enq_id")
    private Enq enqId;

    @Builder
    public Report(Long id, Member memberId, Enq enqId) {
        this.id = id;
        this.memberId = memberId;
        this.enqId = enqId;
    }
}
