package com.sfar.livrili.Mapper;

import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.DeliveryGuyPackResponseDto;
import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.OfferInPackDeliveryGuyDto;
import com.sfar.livrili.Domains.Entities.Pack;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
@Slf4j
public class PacksForDeliveryGuyMapper {
   public DeliveryGuyPackResponseDto DeliveryGuyPackResponseDto(Pack pack) {
       if (pack == null) {
           log.error("Pack is null");
           throw new RuntimeException("Pack is null");
       }
       return
               DeliveryGuyPackResponseDto.builder()
               .id(pack.getId())
               .pickUpLocation(pack.getPickUpLocation())
               .description(pack.getDescription())
               .dropOffLocation(pack.getDropOffLocation())
               .weight(pack.getWeight())
               .status(pack.getStatus())
                       .createdAt(pack.getCreatedAt())
               .createdBy(pack.getClient().getFirstName()+" " +pack.getClient().getLastName())
               .offersInPack(pack.getOffers().stream().map(offer -> OfferInPackDeliveryGuyDto.builder()
                       .deliveryGuyName(offer.getDeliveryPerson().getFirstName()+" "+offer.getDeliveryPerson().getLastName())
                       .price(offer.getPrice())
                       .daysToDeliver(offer.getDaysToGetDelivered())
                       .createdOn(offer.getCreatedAt())

                       .build()).collect(Collectors.toList()))
               .build();

   }

}
