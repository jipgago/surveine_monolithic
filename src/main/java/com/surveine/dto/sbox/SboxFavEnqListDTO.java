package com.surveine.dto.sbox;

import com.surveine.dto.cbox.CboxSboxDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 좋아요 한 탬플릿 List 제공 DTO
 */
@Getter
public class SboxFavEnqListDTO {
    private List<CboxSboxDTO> cbList;
    private List<SboxFavEnqDTO> favEnqList;
    @Builder

    public SboxFavEnqListDTO(List<CboxSboxDTO> cbList, List<SboxFavEnqDTO> favEnqList) {
        this.cbList = cbList;
        this.favEnqList = favEnqList;
    }
}
