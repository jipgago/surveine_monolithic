package com.surveine.repository;

import com.surveine.domain.Fav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavRepository extends JpaRepository<Fav, Long> {
    List<Fav> findByMemberId(Long memberId);

    Optional<Fav> findByEnqId(Long enqId);
}
