package com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto;

import com.sfar.livrili.Domains.Entities.OfferStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OfferResDto {
    private UUID id;
    private Double price;
    private Integer dayToDeliver;
    private OfferStatus status;
    private LocalDateTime createdAt;
}
