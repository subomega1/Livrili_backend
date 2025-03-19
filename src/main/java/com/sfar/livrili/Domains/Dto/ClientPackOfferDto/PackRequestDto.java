package com.sfar.livrili.Domains.Dto.ClientPackOfferDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackRequestDto {

    private String description;

    private Float weight;

    private String pickUpLocation;

    private String dropOffLocation;

}
