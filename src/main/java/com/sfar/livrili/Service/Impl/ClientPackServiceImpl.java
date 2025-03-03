package com.sfar.livrili.Service.Impl;

import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.OfferClientDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackResponseDto;
import com.sfar.livrili.Domains.Entities.Client;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Domains.Entities.PackageStatus;
import com.sfar.livrili.Repositories.ClientRepository;
import com.sfar.livrili.Repositories.PackRepository;
import com.sfar.livrili.Service.ClientPackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientPackServiceImpl implements ClientPackService {
    private final ClientRepository clientRepository;
    private final PackRepository packRepository;
    @Override
    public Pack CreatePackForClient(UUID userId, PackRequestDto packRequest) {

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

        Pack savedPack = packRepository.save(newPack);

        return savedPack;



    }


    @Override
    public List<Pack> GetAllPacks(UUID userId) {
        if (userId == null || !clientRepository.existsById(userId)) {
            throw new IllegalArgumentException("Client not found");
        }
        return packRepository.findAllByClientId(userId);
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
        if (packStatus.equals(PackageStatus.PENDING.name()) || packStatus.equals(PackageStatus.OFFERED.name())){
            try {
                packRepository.deleteByClientIdAndId(userId, packId);
            }catch (Exception e){
                throw new IllegalArgumentException("Pack cannot be deleted");
            }
        }else {
            throw new IllegalStateException("Pack cannot be deleted because it's status");
        }



    }
}
