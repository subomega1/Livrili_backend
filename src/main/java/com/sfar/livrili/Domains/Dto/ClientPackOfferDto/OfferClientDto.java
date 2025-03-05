package com.sfar.livrili.Domains.Dto.ClientPackOfferDto;

import com.sfar.livrili.Domains.Entities.OfferStatus;
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
    private Integer nbDaysToDeliver;
    private OfferStatus offerStatus;
    LocalDateTime createdAt;
}
