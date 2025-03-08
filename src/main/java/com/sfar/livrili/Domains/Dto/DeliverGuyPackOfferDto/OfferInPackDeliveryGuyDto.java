package com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferInPackDeliveryGuyDto {
    private  String deliveryGuyName;
    private  Float deliveryGuyRating;
    private int deliverGuyRatingCount;
    private Double price;
    private int daysToDeliver;
    LocalDateTime createdOn;

}
