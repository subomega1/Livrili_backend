package com.sfar.livrili.Service.Impl;

import com.sfar.livrili.Domains.Dto.PackRequestDto;
import com.sfar.livrili.Domains.Entities.Client;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Domains.Entities.PackageStatus;
import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Repositories.ClientRepository;
import com.sfar.livrili.Repositories.PackageRepository;
import com.sfar.livrili.Repositories.UserRepository;
import com.sfar.livrili.Service.PackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PackServiceImpl implements PackService {
    private final ClientRepository clientRepository;
    private final PackageRepository packageRepository;
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

        Pack savedPack = packageRepository.save(newPack);

        return newPack;



    }
}
