package com.sfar.livrili.Domains.Dto;

import com.sfar.livrili.Domains.Entities.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackResponseDto {
    private UUID id;

    private String description;

    private String weight;

    private String pickUpLocation;

    private String dropOffLocation;

    private PackageStatus status;
}
