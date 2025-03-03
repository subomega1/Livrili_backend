package com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto;

import com.sfar.livrili.Domains.Entities.OfferStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OfferResDto {
    private UUID id;
    private Double price;
    private OfferStatus status;
    private LocalDateTime createdAt;
}
