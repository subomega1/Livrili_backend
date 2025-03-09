package com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryGuyPackResponseDto {
    private UUID id;
    private String clientName;
    private String description;
    private String pickUpLocation;
    private String dropOffLocation;
    private PackageStatus status;
    private Float weight;
    private LocalDateTime createdAt;
    private String createdBy;
    private List<OfferInPackDeliveryGuyDto> offersInPack = new ArrayList<>();



}

