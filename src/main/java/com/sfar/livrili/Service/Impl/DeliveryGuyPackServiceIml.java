package com.sfar.livrili.Service.Impl;


import com.sfar.livrili.Domains.Dto.OfferRequest;
import com.sfar.livrili.Domains.Entities.*;
import com.sfar.livrili.Repositories.DeliveryPersonRepository;
import com.sfar.livrili.Repositories.OfferRepository;
import com.sfar.livrili.Repositories.PackRepository;
import com.sfar.livrili.Service.DeliveryGuyPackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryGuyPackServiceIml implements DeliveryGuyPackService {


    private final PackRepository packRepository;
    private final OfferRepository offerRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;

    @Override
    public List<Pack> getPacks() {
        return packRepository.findAll().stream().filter(pack -> pack.getStatus().equals(PackageStatus.PENDING) || pack.getStatus().equals(PackageStatus.OFFERED)).collect(Collectors.toList());
    }

    @Override
    public Offer CreateOffer(OfferRequest offer , UUID userId ,UUID packId) {
        if (packId == null) {
            throw new IllegalArgumentException("Pack id cannot be null");
        }
        Pack pack = packRepository.findById(packId).orElseThrow( () -> new IllegalArgumentException("Pack not found"));
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(userId).orElseThrow( () -> new IllegalArgumentException("Delivery person not found"));

        if ( pack.getStatus().equals(PackageStatus.DELIVERED)) {
            throw new IllegalArgumentException("Pack already delivered");
        }
        if (pack.getStatus() == PackageStatus.APPROVED) {
            throw new IllegalArgumentException("Pack already approved an offer");
        }
        if (offerRepository.existsByDeliveryPerson_Id(deliveryPerson.getId())) {
            throw new IllegalArgumentException("You are already give an offer");
        }
        Offer newOffer = Offer.builder()
                .price(offer.getPrice())
                .pack(pack)
                .deliveryPerson(deliveryPerson)
                .status(OfferStatus.PENDING)
                .build();
    offerRepository.save(newOffer);
    return newOffer;
    }

}
