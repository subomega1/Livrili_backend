package com.sfar.livrili.Service.Impl;


import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Domains.Entities.PackageStatus;
import com.sfar.livrili.Repositories.PackRepository;
import com.sfar.livrili.Service.DeliveryGuyPackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryGuyPackServiceIml implements DeliveryGuyPackService {


    private final PackRepository packRepository;

    @Override
    public List<Pack> getPacks() {
        return packRepository.findAll().stream().filter(pack -> pack.getStatus().equals(PackageStatus.PENDING) || pack.getStatus().equals(PackageStatus.OFFERED)).collect(Collectors.toList());
    }
}
