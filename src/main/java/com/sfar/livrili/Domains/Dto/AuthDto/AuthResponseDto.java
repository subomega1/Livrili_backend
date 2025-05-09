package com.sfar.livrili.Domains.Dto.AuthDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponseDto {
    private String token;
    private long expiresIn;
    private String message;
}
