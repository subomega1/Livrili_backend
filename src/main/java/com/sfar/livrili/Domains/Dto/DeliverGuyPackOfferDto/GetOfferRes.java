package com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;



@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class GetOfferRes extends OfferResDto {

    private UUID packId;


}
