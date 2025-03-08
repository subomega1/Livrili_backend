package com.sfar.livrili.Service.Impl;


import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.OfferDecisionRequest;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackResponseDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.RattingRequestDto;
import com.sfar.livrili.Domains.Entities.*;
import com.sfar.livrili.Repositories.ClientRepository;
import com.sfar.livrili.Repositories.DeliveryPersonRepository;
import com.sfar.livrili.Repositories.OfferRepository;
import com.sfar.livrili.Repositories.PackRepository;
import com.sfar.livrili.Service.ClientPackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientPackServiceImpl implements ClientPackService {
    private final ClientRepository clientRepository;
    private final PackRepository packRepository;
    private final OfferRepository offerRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;

    @Override
    public Pack createPackForClient(UUID userId, PackRequestDto packRequest) {

        Client client = clientRepository.findById(userId).orElseThrow(()-> new RuntimeException("Client not found"));
        if (packRequest.getPickUpLocation() == null){
            packRequest.setPickUpLocation(client.getAddress());
        }
        Pack newPack = Pack.builder()
                .client(client)
                .description(packRequest.getDescription())
                .weight(packRequest.getWeight())
                .pickUpLocation(packRequest.getPickUpLocation())
                .dropOffLocation(packRequest.getDropOffLocation())
                .status(PackageStatus.PENDING)
                .build();

        return packRepository.save(newPack);



    }


    @Override
    public List<Pack> getAllPacks(UUID userId) {
        if (userId == null || !clientRepository.existsById(userId)) {
            throw new IllegalArgumentException("Client not found");
        }

        List<Pack> packs = packRepository.findAllByClientId(userId);

        packs.forEach(pack -> {
            if (pack.getStatus().equals(PackageStatus.APPROVED)) {
                // Keep only the accepted offers in the response but don't modify the entity
                pack.getOffers().removeIf(offer -> !offer.getStatus().equals(OfferStatus.ACCEPTED));
            }
        });

        return packs;
    }


    @Override
    public PackResponseDto modifyPack(UUID userId, PackRequestDto updatedPack,UUID packId) {
        if (!clientRepository.existsById(userId)){
            throw new IllegalArgumentException("Client not found");
        }
        Pack oldPack = packRepository.findByClientIdAndId(userId, packId).orElseThrow(()-> new IllegalArgumentException("Pack not found"));
        if (oldPack.getStatus().equals(PackageStatus.PENDING)){
            if (updatedPack.getDescription() != null) {
                oldPack.setDescription(updatedPack.getDescription());
            }
            if (updatedPack.getWeight() != null) {
                oldPack.setWeight(updatedPack.getWeight());
            }
            if (updatedPack.getPickUpLocation() != null) {
                oldPack.setPickUpLocation(updatedPack.getPickUpLocation());
            }
            if (updatedPack.getDropOffLocation() != null) {
                oldPack.setDropOffLocation(updatedPack.getDropOffLocation());
            }
            Pack savedPack = packRepository.save(oldPack);
            return  PackResponseDto.builder()
                    .id(savedPack.getId())
                    .description(oldPack.getDescription())
                    .weight(oldPack.getWeight())
                    .pickUpLocation(oldPack.getPickUpLocation())
                    .dropOffLocation(oldPack.getDropOffLocation())
                    .status(oldPack.getStatus())
                    .build();

        }
        else {
            throw new IllegalStateException("Pack cannot be modified while having offers");
        }

    }

    @Override
    public void deletePack(UUID userId, UUID packId) {
        if (!clientRepository.existsById(userId)){
            throw new IllegalArgumentException("Client not found");
        }
        String packStatus = packRepository.findPackStatusByClientIdAndPackId(userId, packId).orElseThrow(()-> new IllegalArgumentException("Pack not found"));
        if (packStatus.equals(PackageStatus.PENDING.name()) || packStatus.equals(PackageStatus.OFFERED.name()) || packStatus.equals(PackageStatus.RATED.name())){
            try {
                packRepository.deleteByClientIdAndId(userId, packId);
            }catch (Exception e){
                throw new IllegalArgumentException("Pack cannot be deleted");
            }
        }else {
            throw new IllegalStateException("Pack cannot be deleted because it's status");
        }



    }
    @Override
    public String approvePackOrDeclineOffer(UUID userId, UUID offerId, OfferDecisionRequest offerDecisionRequest) {
        if (userId == null || !clientRepository.existsById(userId)) {
            throw new IllegalArgumentException("Client not found");
        }

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

        if (!offer.getPack().getClient().getId().equals(userId)) {
            throw new IllegalArgumentException("User doesn't have this offer to approve");
        }

        if (offer.getPack().getStatus().equals(PackageStatus.APPROVED)) {
            throw new IllegalArgumentException("Pack already approved");
        }

        if (offer.getPack().getStatus().equals(PackageStatus.DELIVERED)) {
            throw new IllegalArgumentException("Pack already delivered");
        }

        if (offer.getStatus().equals(OfferStatus.DECLINED)) {
            throw new IllegalArgumentException("The offer is already declined");
        }

        if (offer.getStatus().equals(OfferStatus.ACCEPTED)) {
            throw new IllegalArgumentException("The offer is already accepted");
        }

        if (offerDecisionRequest.getStatus() == null) {
            throw new IllegalArgumentException("Offer decision request status is null");
        }

        Pack pack = packRepository.findPackById(offer.getPack().getId())
                .orElseThrow(() -> new IllegalArgumentException("Pack not found"));

        if (offerDecisionRequest.getStatus().equals(OfferStatus.ACCEPTED)) {
            // Accept the selected offer
            offer.setStatus(OfferStatus.ACCEPTED);

            // Decline all other offers for the same package
            List<Offer> otherOffers = pack.getOffers().stream()
                    .filter(o -> !o.getId().equals(offerId)) // Exclude the accepted offer
                    .toList();

            otherOffers.forEach(o -> o.setStatus(OfferStatus.DISPOSED));
            offerRepository.saveAll(otherOffers); // Save all declined offers

            // Update package status to approved
            pack.setStatus(PackageStatus.APPROVED);
            offerRepository.save(offer); // Save the accepted offer
            packRepository.save(pack);

            return "Offer approved and other offers declined";
        } else {
            // Just decline the selected offer
            offer.setStatus(OfferStatus.DECLINED);
            offerRepository.save(offer);
            return "Offer declined";
        }
    }

    @Override
    public List<Pack> getApprovedPacks(UUID userId) {
        if (userId == null || !clientRepository.existsById(userId)) {
            throw new IllegalArgumentException("Client not found");
        }
        return packRepository.getApprovedPacksByClientId(userId,PackageStatus.APPROVED).orElse(new ArrayList<>());
    }

    @Override
    public List<Pack> getDeliveredPacks(UUID userId) {
        if (userId == null || !clientRepository.existsById(userId)) {
            throw new IllegalArgumentException("Client not found");
        }
        return packRepository.getApprovedPacksByClientId(userId,PackageStatus.RATED).orElse(new ArrayList<>());
    }

    @Override
    public Pack giveRatting(UUID userId, UUID packId, RattingRequestDto rattingRequestDto) {
        if (userId == null || !clientRepository.existsById(userId)) {
            throw new IllegalArgumentException("Client not found");
        }
        if (packId == null || !packRepository.existsById(packId)) {
            throw new IllegalArgumentException("Pack not found");
        }

        Pack pack = packRepository.findByClientIdAndId(userId, packId)
                .orElseThrow(() -> new IllegalArgumentException("This pack does not exist"));

        if (pack.getStatus() != PackageStatus.DELIVERED) {
            throw new IllegalStateException("Pack is not delivered yet");
        }

        Optional<Offer> offerOfThePack = pack.getOffers().stream()
                .filter(offer -> offer.getStatus().equals(OfferStatus.ACCEPTED))
                .findFirst();

        if (offerOfThePack.isEmpty()) {
            throw new IllegalStateException("This offer doesn't exist");
        }

        Offer offer = offerOfThePack.get();
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(offer.getDeliveryPerson().getId())
                .orElseThrow(() -> new IllegalArgumentException("This person does not exist"));

        if (rattingRequestDto.getRating() != null) {
            if (rattingRequestDto.getRating() > 5 || rattingRequestDto.getRating() < 0) {
                throw new IllegalArgumentException("Rating is out of range");
            }

            if (deliveryPerson.getRating() == -1) { // First rating
                deliveryPerson.setRating(rattingRequestDto.getRating());
                deliveryPerson.setRatingCount(1);
            } else {
                deliveryPerson.setRatingCount(deliveryPerson.getRatingCount() + 1);
                float newRating = (deliveryPerson.getRating() * (deliveryPerson.getRatingCount() - 1) + rattingRequestDto.getRating()) / deliveryPerson.getRatingCount();
                deliveryPerson.setRating(newRating);
            }

            deliveryPersonRepository.save(deliveryPerson);
        }

        pack.setStatus(PackageStatus.RATED);
        return packRepository.save(pack);
    }



}
