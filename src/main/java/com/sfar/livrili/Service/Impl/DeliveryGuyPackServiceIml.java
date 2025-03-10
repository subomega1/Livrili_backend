package com.sfar.livrili.Service.Impl;


import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.OfferRequest;
import com.sfar.livrili.Domains.Dto.ErrorDto.FieldsError;
import com.sfar.livrili.Domains.Dto.ErrorDto.IllegalArgs;
import com.sfar.livrili.Domains.Entities.*;
import com.sfar.livrili.Repositories.DeliveryPersonRepository;
import com.sfar.livrili.Repositories.OfferRepository;
import com.sfar.livrili.Repositories.PackRepository;
import com.sfar.livrili.Service.DeliveryGuyPackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<FieldsError> errors = validateOfferCreation(offer);
        if (!errors.isEmpty()) {
            throw new IllegalArgs("Offer cannot be created",errors);
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
    public Offer UpdateOffer(OfferRequest offer, UUID userId, UUID offerId) {
        if (offer == null) {
            throw new IllegalArgumentException("Offer cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (!deliveryPersonRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        Offer offerToUpdate = offerRepository.findOfferByIdAndUserId(userId, offerId).orElseThrow( () -> new IllegalArgumentException("Offer not found"));
        if (offerToUpdate.getStatus() == OfferStatus.ACCEPTED) {
            throw new IllegalStateException("Offer already accepted");
        }
        if (offerToUpdate.getStatus() == OfferStatus.DISPOSED) {
            throw new IllegalStateException("Offer already disposed");
        }
        if (offer.getPrice() == null && offer.getDayToDeliver()==null){
            throw new IllegalArgumentException("At least one field must be updated");
        }
        List<FieldsError> errors = new ArrayList<>(); // Validate the offer>
        if (offer.getPrice() != null){
            if (offer.getPrice() < 0) {
                errors.add(new FieldsError("price", "Price must be positive"));
            }
        }
        if (offer.getDayToDeliver() != null){
            if (offer.getDayToDeliver() < 0) {
                errors.add(new FieldsError("dayToDeliver", "Day to deliver must be positive"));
            }

        }
        if (!errors.isEmpty()){
            throw new IllegalArgs("can't update offer",errors);
        }
        if (offer.getPrice() != null){
            offerToUpdate.setPrice(offer.getPrice());
        }
        if (offer.getDayToDeliver() != null){
            offerToUpdate.setDaysToGetDelivered(offer.getDayToDeliver());
        }

        offerToUpdate.setStatus(OfferStatus.PENDING);
        return offerRepository.save(offerToUpdate);
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

    @Transactional
    @Override
    public void deleteOffer(UUID userId, UUID offerId) {
        if (!deliveryPersonRepository.existsById(userId)) {
            throw new IllegalArgumentException("This user doesn't exist");
        }
        if (!offerRepository.existsById(offerId)) {
            throw new IllegalArgumentException("This offer doesn't exist");
        }

        Offer offerToDelete = offerRepository.findOfferByIdAndUserId(userId, offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer for this User not found"));

        UUID packId = offerToDelete.getPack().getId();

        // Delete the offer
        offerRepository.deleteOfferByIdAndUserId(userId, offerId);

        // Check if the pack has any remaining offers
        boolean hasOtherOffers = offerRepository.existsByPackId(packId);

        if (!hasOtherOffers) {
            Pack pack = packRepository.findById(packId)
                    .orElseThrow(() -> new IllegalArgumentException("Pack not found"));
            pack.setStatus(PackageStatus.PENDING);
            packRepository.save(pack);
        }
    }

    @Override
    public List<Pack> packsToDeliver(UUID userId) {
        if (userId == null || !deliveryPersonRepository.existsById(userId)) {
            throw new IllegalArgumentException("This user doesn't exist");
        }

        List<Pack> approvedPacks = packRepository.getApprovedPacks(PackageStatus.APPROVED).orElseThrow((() -> new IllegalArgumentException("No approved packs")));



        return approvedPacks.stream()
                .filter(pack -> pack.getOffers().stream()
                        .anyMatch(offer -> offer.getDeliveryPerson().getId().equals(userId) &&
                                offer.getStatus().equals(OfferStatus.ACCEPTED))) // Only accepted offers
                .toList();
    }

    @Override
    public Pack deliverPack(UUID userId, UUID packId) {
        if (userId == null || !deliveryPersonRepository.existsById(userId)) {
            throw new IllegalArgumentException("This user doesn't exist");
        }
        if (packId == null ) {
            throw new IllegalArgumentException("This packId cannot be null");
        }
        UUID deliveryGuyPackToDeliverId = offerRepository.PackToDeliver(userId,packId,OfferStatus.ACCEPTED,PackageStatus.APPROVED).orElseThrow( () -> new IllegalArgumentException("No approved pack for this User"));
        if (!deliveryGuyPackToDeliverId.equals(packId)) {
            throw new IllegalArgumentException("This User do not have this approved pack");
        }
        Pack deliveredPack = packRepository.findPackById(packId).orElseThrow( () -> new IllegalArgumentException("Pack not found"));
        deliveredPack.setStatus(PackageStatus.DELIVERED);
        return packRepository.save(deliveredPack);



    }

    @Override
    public List<Pack> deliveredPacks(UUID userId) {
        if (userId == null || !deliveryPersonRepository.existsById(userId)) {
            throw new IllegalArgumentException("This user doesn't exist");
        }
        return offerRepository.getDeliveredPacks(userId,OfferStatus.ACCEPTED,PackageStatus.DELIVERED);
    }

    private List<FieldsError> validateOfferCreation(OfferRequest offer) {
        List<FieldsError> errors = new ArrayList<>();
        if (offer.getPrice() == null) {
            errors.add(new FieldsError("price", "Price is required"));
        }else if (offer.getPrice() < 0) {
            errors.add(new FieldsError("price", "Price must be positive"));
        }
        if (offer.getDayToDeliver() == null) {
            errors.add(new FieldsError("dayToDeliver", "Day to deliver is required"));
        }else{
            if (offer.getDayToDeliver() < 0) {
                errors.add(new FieldsError("dayToDeliver", "Day to deliver must be positive"));
            }
        }
        return errors;
    }

}
