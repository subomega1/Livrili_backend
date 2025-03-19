package com.sfar.livrili.Domains.Dto.ClientPackOfferDto;

import com.sfar.livrili.Domains.Entities.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackResponseDto {
    private UUID id;

    private String description;

    private Float weight;

    private String pickUpLocation;

    private String dropOffLocation;

    private PackageStatus status;

    LocalDateTime createdAt;

    private List<OfferClientDto> offers = new ArrayList<>();
}
