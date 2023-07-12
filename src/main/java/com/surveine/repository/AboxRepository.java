package com.surveine.repository;

import com.surveine.domain.Abox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AboxRepository extends JpaRepository<Abox, Long> {
    List<Abox> findByMemberId(Long memberId);
}
