package com.surveine.repository;

import com.surveine.domain.Cbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CboxRepository extends JpaRepository<Cbox, Long> {

    List<Cbox> findByMemberId(Long memberId);
}
