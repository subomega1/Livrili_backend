package com.sfar.livrili.Domains.Dto;
import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.OfferResDto;
import com.sfar.livrili.Domains.Entities.PackageStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder

public class ApprovedPackDto {
    private UUID packId;


    private String packDescription;

    private String packWeight;

    private String packPickUpLocation;

    private String packDropOffLocation;

    private PackageStatus packStatus;

    private LocalDateTime packCreationDate;

    private String clientName;

    private String clientPhone;

    OfferResDto offer;

    private String deliveryGuyName;

    private Float deliveryGuyRating;

    private String deliveryGuyPhone;



}
