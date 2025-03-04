package com.sfar.livrili.Mapper;

import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.GetOfferRes;
import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.OfferResDto;
import com.sfar.livrili.Domains.Entities.Offer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Slf4j
public class OfferForDeliveryGuyMapper {

    public OfferResDto toOfferResDto(Offer offer) {
        if(offer == null) {
            log.error("offer is null");
            throw new RuntimeException("offer is null");
        }
        return OfferResDto.builder()
                .id(offer.getId())
                .price(offer.getPrice())
                .status(offer.getStatus())
                .createdAt(offer.getCreatedAt())
                .build();
    }
    public GetOfferRes toGetOfferRes(Offer offer) {
        if(offer == null) {
            log.error("offer is null");
            throw new RuntimeException("offer is null");
        }
        return GetOfferRes.builder()
                .id(offer.getId())
                .price(offer.getPrice())
                .status(offer.getStatus())
                .createdAt(offer.getCreatedAt())
                .dayToDeliver(offer.getDaysToGetDelivered())  // Assuming offerId is the same as offer.getId()
                .packId(offer.getPack().getId())  // Assuming pack has an ID
                .build();
    }
}
