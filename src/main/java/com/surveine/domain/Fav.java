package com.surveine.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Fav")
public class Fav {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fav_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enq_id")
    private Enq enq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Fav(Long id, Enq enq, Member member) {
        this.id = id;
        this.enq = enq;
        this.member = member;
    }
}
