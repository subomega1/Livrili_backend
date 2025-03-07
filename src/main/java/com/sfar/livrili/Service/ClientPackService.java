package com.sfar.livrili.Service;


import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.OfferDecisionRequest;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackResponseDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.RattingRequestDto;
import com.sfar.livrili.Domains.Entities.Pack;

import java.util.List;
import java.util.UUID;

public interface ClientPackService {

     Pack createPackForClient(UUID userId , PackRequestDto pack);
     List<Pack> getAllPacks(UUID userId);
     PackResponseDto modifyPack(UUID userId, PackRequestDto pack, UUID packId);
     void deletePack(UUID userId, UUID packId);
     String approvePackOrDeclineOffer(UUID userId, UUID offerId , OfferDecisionRequest offerDecisionRequest);
     List<Pack> getApprovedPacks(UUID userId);
     List<Pack>getDeliveredPacks(UUID userId);
     Pack giveRatting(UUID userId, UUID packId, RattingRequestDto rattingRequestDto);
}
