package com.sfar.livrili.Domains.Dto;

import com.sfar.livrili.Domains.Entities.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferRequest {
    private Double price;

}
