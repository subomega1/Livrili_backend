package com.sfar.livrili.Domains.Dto.ClientPackOfferDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OfferClientDto {
    private String deliveryGuyName;
    private Double deliveryGuyPrice;
    private int nbDaysToDeliver;
    LocalDateTime createdAt;
}
