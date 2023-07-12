package com.surveine.dto.cbox;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class EnqNameChangeDTO {
    //CboxWSpaceEnqNameChangeDTO -> EnqNameChangeDTO 클래스명 변경
    String enqName;
    @Builder(toBuilder = true)
    public EnqNameChangeDTO(String enqName){
        this.enqName = enqName;
    }
}


//@Getter
//public class EnqNameChangeDTO {
//    @NotNull
//    Long enqID;
//    String enqName;
//    @Builder(toBuilder = true)
//    public EnqNameChangeDTO(Long enqID,String enqName){
//        this.enqID = enqID;
//        this.enqName = enqName;
//    }
//}


//package com.surveine.dto.cbox;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.validation.constraints.NotNull;
//
//@Getter
//@NoArgsConstructor
//public class EnqNameChangeDTO {
//    @NotNull
//    Long enqID;
//    String enqName;
//
//    @Builder(toBuilder = true)
//    public EnqNameChangeDTO(Long enqID,String enqName){
//        this.enqID = enqID;
//        this.enqName = enqName;
//    }
//}
