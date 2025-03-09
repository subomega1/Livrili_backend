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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dg/pack")
@RequiredArgsConstructor
@Tag(name = "Delivery Guy Pack Management", description = "Operations related to Delivery Guy packages")

public class DeliveryGuyPackController {

    private final DeliveryGuyPackService deliveryGuyPackService;
    private final PacksForDeliveryGuyMapper packsForDeliveryGuyMapper;
    private final OfferForDeliveryGuyMapper offerForDeliveryGuyMapper;
    private final ApprovedPackMapper approvedPackMapper;

    @Operation(summary = "Get all packs  for the delivery guy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved packs"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<DeliveryGuyPackResponseDto>> getAllPacks(HttpServletRequest req) {
        UUID userId = (UUID) req.getAttribute("userId");
        List<Pack> packs = deliveryGuyPackService.getPacks(userId);
        List<DeliveryGuyPackResponseDto> packsToShow = packs.stream()
                .map(packsForDeliveryGuyMapper::DeliveryGuyPackResponseDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(packsToShow, HttpStatus.OK);
    }

    @Operation(summary = "Give an offer for a specific pack")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Offer created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/offer/{id}")
    public ResponseEntity<OfferResDto> giveOffer(
            @PathVariable UUID id,
            @RequestBody OfferRequest offerRequest,
            HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        Offer offer = deliveryGuyPackService.CreateOffer(offerRequest, userId, id);
        return new ResponseEntity<>(offerForDeliveryGuyMapper.toOfferResDto(offer), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all offers for the delivery guy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved offers")
    })
    @GetMapping("/offer")
    public ResponseEntity<List<GetOfferRes>> getOffer(HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        List<Offer> offers = deliveryGuyPackService.getOffers(userId);
        List<GetOfferRes> offerRes = offers.stream()
                .map(offerForDeliveryGuyMapper::toGetOfferRes)
                .collect(Collectors.toList());
        return new ResponseEntity<>(offerRes, HttpStatus.OK);
    }

    @Operation(summary = "Get all approved packs for delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved approved packs"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/approved")
    public ResponseEntity<List<ApprovedPackDto>> getApprovedPacks(HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        List<Pack> packs = deliveryGuyPackService.packsToDeliver(userId);
        List<ApprovedPackDto> approvedPacks = packs.stream()
                .map(approvedPackMapper::toApprovedPackDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);
    }

    @Operation(summary = "Get all delivered packs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved delivered packs")
    })
    @GetMapping("/delivered")
    public ResponseEntity<List<ApprovedPackDto>> getDeliveredPacks(HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        List<Pack> packs = deliveryGuyPackService.deliveredPacks(userId);
        List<ApprovedPackDto> approvedPacks = packs.stream()
                .map(approvedPackMapper::toApprovedPackDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated offer"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping("/offer/{id}")
    public ResponseEntity<OfferResDto> updateOffer(
            @PathVariable UUID id,
            @RequestBody OfferRequest offerRequest,
            HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        Offer offer = deliveryGuyPackService.UpdateOffer(offerRequest, userId, id);
        return new ResponseEntity<>(offerForDeliveryGuyMapper.toOfferResDto(offer), HttpStatus.OK);
    }

    @Operation(summary = "Mark a pack as delivered")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pack marked as delivered"),
            @ApiResponse(responseCode = "404", description = "Pack not found")
    })
    @PutMapping("/{id}/delivered")
    public ResponseEntity<ApprovedPackDto> deliver(
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        Pack pack = deliveryGuyPackService.deliverPack(userId, id);
        return new ResponseEntity<>(approvedPackMapper.toApprovedPackDto(pack), HttpStatus.OK);
    }

    @Operation(summary = "Delete an offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @DeleteMapping("/offer/{id}")
    public ResponseEntity<String> deleteOffer(
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        deliveryGuyPackService.deleteOffer(userId, id);
        return new ResponseEntity<>("Offer was deleted successfully", HttpStatus.OK);
    }
}
