package com.sfar.livrili.Domains.Dto.AuthDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyAuthRes {
    
    private AuthResponseDto authResponseDto;
    private Object clientOrDeliveryGuy;
}
