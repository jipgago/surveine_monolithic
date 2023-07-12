package com.surveine.repository;

import com.surveine.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmailAndPassword(String email, String password);

    Boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
