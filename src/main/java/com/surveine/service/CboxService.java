package com.surveine.service;

import com.surveine.domain.Cbox;
import com.surveine.domain.Enq;
import com.surveine.dto.cbox.EnqNameChangeDTO;
import com.surveine.repository.CboxRepository;
import com.surveine.repository.EnqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CboxService {
    private final CboxRepository cboxRepository;
    private final EnqRepository enqRepository;

    @Transactional
    public void create(Cbox cbox) {
        cboxRepository.save(cbox);
    }

    public List<Cbox> getCBoxesByMemberId(Long memberId) {
        return cboxRepository.findByMemberId(memberId);
    }

    public Optional<Cbox> getCBoxById(Long defaultcboxId) {
        return cboxRepository.findById(defaultcboxId);
    }

    public Boolean deleteBox(Long cboxId) {
        Cbox currentCbox = cboxRepository.findById(cboxId).get();
        // cboxId에 해당하는 enq가 하나도 존재하지 않을 때에만 삭제.
        if (enqRepository.countByCboxId(cboxId) == 0L) {
            cboxRepository.delete(currentCbox);
            return true;
        }
        // cboxId에 해당하는 enq가 존재하므로, 삭제 거부
        return false;
    }

    public List<Cbox> getCBoxByMemberId(Long memberId) {
        return cboxRepository.findByMemberId(memberId);
    }

    public Optional<Cbox> findById(Long cboxId) {
        return cboxRepository.findById(cboxId);
    }
}
