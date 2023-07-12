package com.surveine.domain;

import com.surveine.enums.AnsStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Table(name = "ans")
public class Ans {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ans_id")
    private Long id;

    @Column(name = "ans_name")
    private String name;

    @Column(name = "ans_cont", columnDefinition = "TEXT")
    private String cont;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enq_id")
    private Enq enq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abox_id")
    private Abox abox;

    @Column(name = "ans_state")
    @Enumerated(EnumType.STRING)
    private AnsStatus status;

    @Column(name = "ans_is_show")
    private Boolean isShow;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Builder(toBuilder = true)
    public Ans(Long id,String name, String cont, Enq enq, Member member, Abox abox, AnsStatus status, Boolean isShow, LocalDate updateDate){
        this.id = id;
        this.name = name;
        this.cont = cont;
        this.enq = enq;
        this.member = member;
        this.abox = abox;
        this.status = status;
        this.isShow = isShow;
        this.updateDate = updateDate;
    }
}
