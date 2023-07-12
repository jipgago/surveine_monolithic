package com.surveine.domain;

import com.surveine.enums.Authority;
import com.surveine.enums.GenderType;
import com.surveine.enums.Role;

import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "member")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String name;

    private String birthday;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(Long id, String email, String password, String name, String birthday, GenderType gender, Authority authority) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.authority = authority;
        this.gender = gender;
    }

}
