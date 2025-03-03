package com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferRequest {
    private Double price;
    private int dayToDeliver;

}
