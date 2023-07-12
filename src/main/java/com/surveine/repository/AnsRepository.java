package com.surveine.repository;

import com.surveine.domain.Ans;
import com.surveine.domain.Enq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnsRepository extends JpaRepository<Ans, Long> {
    List<Ans> findByAboxId(Long aboxId);

    Optional<Ans> findById(Long ansId);

    Optional<Ans> getAnsById(Long ansId);

    Long countByAboxId(Long aboxId);

    Long countByMemberId(Long memberId);
}
