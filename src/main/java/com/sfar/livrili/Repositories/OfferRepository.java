package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.Offer;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OfferRepository  extends CrudRepository<Offer, UUID> {
}
