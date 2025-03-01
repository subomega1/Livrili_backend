package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.Offer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OfferRepository  extends CrudRepository<Offer, UUID> {
    boolean existsByDeliveryPerson_Id(@Param("userId") UUID id);

}
