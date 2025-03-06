package com.sfar.livrili.Domains.Dto.ClientPackOfferDto;

import com.sfar.livrili.Domains.Entities.OfferStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OfferClientDto {
    private UUID offerId;
    private String deliveryGuyName;
    private Double deliveryGuyPrice;
    private Integer nbDaysToDeliver;
    private OfferStatus offerStatus;
    LocalDateTime createdAt;
}
