package com.sfar.livrili.Service;


import com.sfar.livrili.Domains.Dto.PackRequestDto;
import com.sfar.livrili.Domains.Entities.Pack;

import java.util.UUID;

public interface PackService {

    public Pack CreatePackForClient(UUID userId , PackRequestDto pack);

}
