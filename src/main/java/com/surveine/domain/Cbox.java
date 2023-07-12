package com.surveine.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author 11chyeonjin
 * @discription: cbox Entity
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cbox")
public class Cbox {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 추가 선언 해줘야됨.
    @Column(name = "cbox_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "box_name")
    private String name;

    @Builder(toBuilder = true)
    public Cbox(Long id, Member member, String name) {
        this.id = id;
        this.member = member;
        this.name = name;
    }
}
