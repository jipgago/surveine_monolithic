package com.surveine.repository;

import com.surveine.domain.Enq;
import com.surveine.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByEnqId(Enq enqId);

    Optional<Report> findByMemberId(Long memberId);
}
