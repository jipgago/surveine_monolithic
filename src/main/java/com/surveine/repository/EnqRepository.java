package com.surveine.repository;

import com.surveine.domain.Enq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnqRepository extends JpaRepository<Enq, Long> {
    List<Enq> findByCboxId(Long cboxId);

    Optional<Enq> findById(Long enqId);

    Long countByCboxId(Long cboxId);

    Long countByMemberId(Long memberId);

    Long getCountBycboxId(Long id);

    List<Enq> findByIsSharedTrue();


//    List<Enq> findByEnqStatusOrderByStartDate(String distWait);
//
//    List<Enq> findTop10ByEnqStatusOrderByStartDate(String distWait);

    List<Enq> findTop10ByDistTypeAndEnqStatusOrderByStartDateTime(String link, String distWait);

    Optional<Enq> findByDistLink(String link);
}
