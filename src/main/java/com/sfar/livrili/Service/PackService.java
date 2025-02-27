package com.sfar.livrili.Service;


import com.sfar.livrili.Domains.Dto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.PackResponseDto;
import com.sfar.livrili.Domains.Entities.Pack;

import java.util.List;
import java.util.UUID;

public interface PackService {

     Pack CreatePackForClient(UUID userId , PackRequestDto pack);
     List<PackResponseDto> GetAllPacks(UUID userId);
     PackResponseDto modifyPack(UUID userId, PackRequestDto pack, UUID packId);
}
