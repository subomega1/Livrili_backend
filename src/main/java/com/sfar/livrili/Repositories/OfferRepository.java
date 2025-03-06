package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.Offer;
import com.sfar.livrili.Domains.Entities.OfferStatus;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Domains.Entities.PackageStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferRepository  extends CrudRepository<Offer, UUID> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Offer o WHERE o.deliveryPerson.id = :userId AND o.pack.id = :packId")
    boolean existsByDeliveryPerson_IdAndId(@Param("userId") UUID userId, @Param("packId") UUID packId);


    @Query("select o from Offer o where o.deliveryPerson.id = :userId")
    List<Offer> findByDeliveryPersonId(@Param("userId") UUID userId);


    @Query("select o from Offer o where o.id =:offerId and o.deliveryPerson.id =:userId ")
    Optional<Offer> findOfferByIdAndUserId(@Param("userId") UUID userId, @Param("offerId") UUID id);



    @Modifying
    @Query("DELETE FROM Offer o WHERE o.deliveryPerson.id = :userId AND o.id = :offerId ")
    void deleteOfferByIdAndUserId(@Param("userId") UUID userId, @Param("offerId") UUID id);

    @Query("SELECT COUNT(o) > 0 FROM Offer o WHERE o.pack.id = :packId")
    boolean existsByPackId(@Param("packId") UUID packId);


    @Query("SELECT o.pack.id FROM Offer o WHERE o.status = :offerStatus AND o.deliveryPerson.id = :userId and o.pack.id = :packId and o.pack.status = :packStatus")
    Optional<UUID> PackToDeliver(@Param("userId") UUID userId, @Param("packId") UUID packId, @Param("offerStatus") OfferStatus offerStatus , @Param("packStatus") PackageStatus packStatus);


    @Query("SELECT o.pack FROM Offer o WHERE o.status = :offerStatus AND o.deliveryPerson.id = :userId and o.pack.status = :packStatus")
    List<Pack> getDeliveredPacks(@Param("userId") UUID userId, @Param("offerStatus") OfferStatus offerStatus , @Param("packStatus") PackageStatus packStatus);








}
