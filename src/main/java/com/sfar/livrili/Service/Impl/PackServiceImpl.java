package com.sfar.livrili.Service.Impl;

import com.sfar.livrili.Domains.Dto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.PackResponseDto;
import com.sfar.livrili.Domains.Entities.Client;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Domains.Entities.PackageStatus;
import com.sfar.livrili.Repositories.ClientRepository;
import com.sfar.livrili.Repositories.PackRepository;
import com.sfar.livrili.Service.PackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PackServiceImpl implements PackService {
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
    public List<PackResponseDto> GetAllPacks(UUID userId) {
        if (userId == null || !clientRepository.existsById(userId)){
            throw new IllegalArgumentException("Client not found");
        }
        List<Pack> packs = packRepository.findAllByClientId(userId);
       return packs.stream().map(pack -> PackResponseDto.builder()
               .id(pack.getId())
               .description(pack.getDescription())
               .weight(pack.getWeight())
               .pickUpLocation(pack.getPickUpLocation())
               .dropOffLocation(pack.getDropOffLocation())
               .status(pack.getStatus())
               .build()).toList();
    }
}
