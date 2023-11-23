package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SubscribeDto {
    private int id;
    private String userName;
    private String profileImageUrl;
    private Integer subscribeState;
    private Integer equalUserState;
    //int로하면 마리아디비 오류생길수 있어서 integer 사용

}
