package com.surveine.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "abox")
public class Abox {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abox_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "box_name")
    private String name;

    @Builder(toBuilder = true)
    public Abox(Long id, Member member, String name) {
        this.id = id;
        this.member = member;
        this.name = name;
    }
}
