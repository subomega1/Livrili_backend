package com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto;

import com.sfar.livrili.Domains.Entities.OfferStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;



@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class GetOfferRes extends OfferResDto {
    
    private UUID packId;


}
