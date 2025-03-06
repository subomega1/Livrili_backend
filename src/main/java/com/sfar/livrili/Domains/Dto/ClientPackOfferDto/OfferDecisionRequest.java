package com.sfar.livrili.Domains.Dto.ClientPackOfferDto;

import com.sfar.livrili.Domains.Entities.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferDecisionRequest {
   private OfferStatus status;
}
