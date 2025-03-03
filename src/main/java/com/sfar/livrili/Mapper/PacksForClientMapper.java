package com.sfar.livrili.Mapper;

import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.OfferClientDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackResponseDto;
import com.sfar.livrili.Domains.Entities.Pack;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
@Slf4j
public class PacksForClientMapper {
    public PackResponseDto ToPackResponseDto(Pack pack) {
        if (pack == null) {
            log.info("Pack is null");
            throw new RuntimeException("Pack is null");
        }
        return  PackResponseDto.builder()
                .id(pack.getId())
                .description(pack.getDescription())
                .weight(pack.getWeight())
                .pickUpLocation(pack.getPickUpLocation())
                .dropOffLocation(pack.getDropOffLocation())
                .status(pack.getStatus())
                .createdAt(pack.getCreatedAt())
                .offers(pack.getOffers().stream().map(offer -> OfferClientDto.builder()
                        .deliveryGuyName(offer.getDeliveryPerson().getFirstName() +" " +offer.getDeliveryPerson().getLastName())
                        .deliveryGuyPrice(offer.getPrice())
                        .nbDaysToDeliver(offer.getDaysToGetDelivered())
                        .createdAt(offer.getCreatedAt())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
