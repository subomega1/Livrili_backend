package com.sfar.livrili.Service;


import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackResponseDto;
import com.sfar.livrili.Domains.Entities.Pack;

import java.util.List;
import java.util.UUID;

public interface ClientPackService {

     Pack CreatePackForClient(UUID userId , PackRequestDto pack);
     List<Pack> GetAllPacks(UUID userId);
     PackResponseDto modifyPack(UUID userId, PackRequestDto pack, UUID packId);
     void deletePack(UUID userId, UUID packId);
}
