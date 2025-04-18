package com.sfar.livrili.Service;

import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.OfferRequest;
import com.sfar.livrili.Domains.Entities.Offer;
import com.sfar.livrili.Domains.Entities.Pack;

import java.util.List;
import java.util.UUID;

public interface DeliveryGuyPackService {

    List<Pack> getPacks(UUID userId);

    Offer CreateOffer(OfferRequest offer, UUID userId, UUID packId);

    Offer UpdateOffer(OfferRequest offer, UUID userId, UUID offerId);

    List<Offer> getOffers(UUID userId);

    void deleteOffer(UUID userId, UUID offerId);

    List<Pack> packsToDeliver(UUID userId);

    Pack deliverPack(UUID userId, UUID packId);

    List<Pack> deliveredPacks(UUID userId);
}
