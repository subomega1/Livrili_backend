package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.ApprovedPackDto;
import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.DeliveryGuyPackResponseDto;
import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.GetOfferRes;
import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.OfferRequest;
import com.sfar.livrili.Domains.Dto.DeliverGuyPackOfferDto.OfferResDto;
import com.sfar.livrili.Domains.Entities.Offer;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Mapper.ApprovedPackMapper;
import com.sfar.livrili.Mapper.OfferForDeliveryGuyMapper;
import com.sfar.livrili.Mapper.PacksForDeliveryGuyMapper;
import com.sfar.livrili.Service.DeliveryGuyPackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/DG/pack")
@RequiredArgsConstructor
public class DeliveryGuyPackController {

    private final DeliveryGuyPackService deliveryGuyPackService;
    private final PacksForDeliveryGuyMapper packsForDeliveryGuyMapper;
    private final OfferForDeliveryGuyMapper offerForDeliveryGuyMapper;
    private final ApprovedPackMapper approvedPackMapper;

    @GetMapping
    ResponseEntity<List<DeliveryGuyPackResponseDto>> getAllPacks(HttpServletRequest req) {
        UUID userId = (UUID) req.getAttribute("userId");
       List<Pack> packs = deliveryGuyPackService.getPacks(userId) ;
       List<DeliveryGuyPackResponseDto> packsToShow = packs.stream().map(packsForDeliveryGuyMapper::DeliveryGuyPackResponseDto).collect(Collectors.toList());
        return new ResponseEntity<>(packsToShow, HttpStatus.OK);
    }
    @PostMapping("/offer/{id}")
    ResponseEntity<OfferResDto>giveOffer(@PathVariable UUID id , @RequestBody OfferRequest offerRequest, HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        Offer offer = deliveryGuyPackService.CreateOffer(offerRequest,userId,id);
        return new ResponseEntity<>(offerForDeliveryGuyMapper.toOfferResDto(offer), HttpStatus.CREATED);
    }
    @GetMapping("/offer")
    ResponseEntity<List<GetOfferRes>> getOffer(HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        List<Offer> offers = deliveryGuyPackService.getOffers(userId) ;

        List<GetOfferRes> offerRes = offers.stream().map(offerForDeliveryGuyMapper::toGetOfferRes).toList();
        return new ResponseEntity<>(offerRes, HttpStatus.OK);

    }
    @GetMapping("/approved")
    ResponseEntity<List<ApprovedPackDto>> getApprovedPacks(HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        List<Pack> packs = deliveryGuyPackService.packsToDeliver(userId) ;
        List<ApprovedPackDto> approvedPacks = packs.stream().map(approvedPackMapper::toApprovedPackDto).toList();
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);

    }
    @GetMapping("/delivered")
    ResponseEntity<List<ApprovedPackDto>> getDeliveredPacks(HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        List<Pack> packs = deliveryGuyPackService.deliveredPacks(userId) ;
        List<ApprovedPackDto> approvedPacks = packs.stream().map(approvedPackMapper::toApprovedPackDto).toList();
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);

    }

    @PutMapping("/offer/{id}")
    ResponseEntity<OfferResDto>updateOffer(@PathVariable UUID id , @RequestBody OfferRequest offerRequest, HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        Offer offer = deliveryGuyPackService.UpdateOffer(offerRequest,userId,id);
        return new ResponseEntity<>(offerForDeliveryGuyMapper.toOfferResDto(offer), HttpStatus.OK);
    }

    @PutMapping("/{id}/delivered")
    ResponseEntity<ApprovedPackDto>deliver(@PathVariable UUID id , HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        Pack pack = deliveryGuyPackService.deliverPack(userId, id);
        return new ResponseEntity<>(approvedPackMapper.toApprovedPackDto(pack), HttpStatus.OK);
    }

    @DeleteMapping("/offer/{id}")
    ResponseEntity<String>deleteOffer(@PathVariable UUID id , HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        deliveryGuyPackService.deleteOffer(userId, id);
        return new ResponseEntity<>("Offer was deleted successfully",HttpStatus.OK);
    }

}
