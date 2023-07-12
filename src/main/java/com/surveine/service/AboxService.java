package com.surveine.service;

import com.surveine.domain.Abox;
import com.surveine.domain.Cbox;
import com.surveine.repository.AboxRepository;
import com.surveine.repository.AnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboxService {
    private final AboxRepository aboxRepository;
    private final AnsRepository ansRepository;

    public List<Abox> getAboxesByMemberId(Long memberId) {
        return aboxRepository.findByMemberId(memberId);
    }

    @Transactional
    public void create(Abox abox) {
        aboxRepository.save(abox);
    }

    public Optional<Abox> getAboxById(Long aboxId) {
        return aboxRepository.findById(aboxId);
    }

    public void deleteAbox(Abox abox) {
        aboxRepository.delete(abox);
    }
    public void delete(Abox abox) {
        aboxRepository.delete(abox);
    }

    public Boolean deleteBox(Long aboxId) {
        Abox currentAbox = aboxRepository.findById(aboxId).get();
        // aboxId에 해당하는 ans가 하나도 존재하지 않을 때에만 삭제.
        if (ansRepository.countByAboxId(aboxId) == 0L) {
            aboxRepository.delete(currentAbox);
            return true;
        }
        // aboxId에 해당하는 ans가 존재하므로, 삭제 거부
        return false;
    }
}
