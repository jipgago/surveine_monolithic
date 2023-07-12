package com.surveine.service;

import com.surveine.config.SecurityUtil;
import com.surveine.domain.Enq;
import com.surveine.domain.Fav;
import com.surveine.domain.Member;
import com.surveine.repository.EnqRepository;
import com.surveine.repository.FavRepository;
import com.surveine.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavService {
    private final FavRepository favRepository;
    private final MemberRepository memberRepository;
    private final EnqRepository enqRepository;

    /**
     * 템플릿 좋아요/좋아요 취소
     * @param enqId
     * @return
     */
    @Transactional
    public boolean favHandler(Long enqId) {
        Optional<Fav> fav = favRepository.findByEnqId(enqId);
        Optional<Enq> enq= enqRepository.findById(enqId);
        Long currentFav = enq.get().getFavCount();
        if(fav.isPresent()){
            favRepository.delete(fav.get());
            currentFav -= 1;
            Enq updateFav = enq.get().toBuilder()
                    .favCount(currentFav)
                    .build();
            enqRepository.save(updateFav);
            return false;
        } else {
            try{
                Fav addFav = Fav.builder()
                        .enq(enqRepository.findById(enqId).get())
                        .member(memberRepository.findById(SecurityUtil.getCurrentMemberId()).get())
                        .build();
                favRepository.save(addFav);
                currentFav += 1;
                Enq updateFav = enq.get().toBuilder()
                        .favCount(currentFav)
                        .build();
                enqRepository.save(updateFav);
                return true;
            } catch (EntityNotFoundException e){
                return false;
            }
        }
    }
}
