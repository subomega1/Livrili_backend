package com.sfar.livrili.Service;

import com.sfar.livrili.Domains.Dto.OfferRequest;
import com.sfar.livrili.Domains.Entities.Offer;
import com.sfar.livrili.Domains.Entities.Pack;

import java.util.List;
import java.util.UUID;

public interface DeliveryGuyPackService {

    List<Pack> getPacks();
    Offer CreateOffer(OfferRequest offer, UUID userId,UUID packId);
}
