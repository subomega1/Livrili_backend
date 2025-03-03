package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.Offer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OfferRepository  extends CrudRepository<Offer, UUID> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Offer o WHERE o.deliveryPerson.id = :userId AND o.pack.id = :packId")
    boolean existsByDeliveryPerson_IdAndId(@Param("userId") UUID userId, @Param("packId") UUID packId);

}
