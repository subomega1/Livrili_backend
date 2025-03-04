package com.sfar.livrili.Service.Impl;


import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.OfferRequest;
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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DeliveryGuyPackServiceIml implements DeliveryGuyPackService {


    private final PackRepository packRepository;
    private final OfferRepository offerRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;

    @Override
    public List<Pack> getPacks(UUID userId) {
        List<Pack> packs = packRepository.findAll().stream()
                .filter(pack -> pack.getStatus().equals(PackageStatus.PENDING) || pack.getStatus().equals(PackageStatus.OFFERED))
                .collect(Collectors.toList());

        packs.forEach(pack -> {
            List<Offer> filteredOffers = pack.getOffers().stream()
                    .filter(offer -> !offer.getDeliveryPerson().getId().equals(userId))
                    .collect(Collectors.toList());

            pack.setOffers(filteredOffers); // Ensure filtered offers are set back
        });

        return packs;
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
        if (offerRepository.existsByDeliveryPerson_IdAndId(deliveryPerson.getId(),packId)) {
            throw new IllegalArgumentException("You are already give an offer");
        }
        Offer newOffer = Offer.builder()
                .price(offer.getPrice())
                .pack(pack)
                .deliveryPerson(deliveryPerson)
                .status(OfferStatus.PENDING)
                .daysToGetDelivered(offer.getDayToDeliver())
                .build();
        if (pack.getStatus() != PackageStatus.OFFERED) {
            pack.setStatus(PackageStatus.OFFERED);
        }
        packRepository.save(pack);
    offerRepository.save(newOffer);
    return newOffer;
    }

    @Override
    public Offer UpdateOffer(OfferRequest offer, UUID userId) {
        return null;
    }

    @Override
    public List<Offer> getOffers(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (!deliveryPersonRepository.existsById(userId)) {
            throw new IllegalArgumentException("this user don't exist");
        }
        return offerRepository.findByDeliveryPersonId(userId);
    }

}
