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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Delivery Guy Pack Management", description = "Endpoints for managing packages and offers for delivery guys. Includes operations for retrieving, creating, updating, and deleting packages and offers.")
public class DeliveryGuyPackController {

    private final DeliveryGuyPackService deliveryGuyPackService;
    private final PacksForDeliveryGuyMapper packsForDeliveryGuyMapper;
    private final OfferForDeliveryGuyMapper offerForDeliveryGuyMapper;
    private final ApprovedPackMapper approvedPackMapper;

    @Operation(
            summary = "Get all packs for the delivery guy",
            description = "Retrieves a list of all packages available for the authenticated delivery guy."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved packs",
                    content = @Content(schema = @Schema(implementation = DeliveryGuyPackResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token")
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

    @Operation(
            summary = "Create an offer for a specific pack",
            description = "Allows a delivery guy to create an offer for a specific package. The offer details are provided in the request body."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Offer created successfully",
                    content = @Content(schema = @Schema(implementation = OfferResDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid or incomplete data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token")

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

    @Operation(
            summary = "Get all offers for the delivery guy",
            description = "Retrieves a list of all offers created by the authenticated delivery guy."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivered packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApprovedPackDto.class))),
            @ApiResponse(responseCode = "200", description = "Successfully retrieved offers",
                    content = @Content(schema = @Schema(implementation = GetOfferRes.class)))
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

    @Operation(
            summary = "Get all approved packs for delivery",
            description = "Retrieves a list of all packages that have been approved for delivery by the authenticated delivery guy."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved approved packs",
                    content = @Content(schema = @Schema(implementation = ApprovedPackDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token")
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

    @Operation(
            summary = "Get all delivered packs",
            description = "Retrieves a list of all packages that have been marked as delivered by the authenticated delivery guy."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved delivered packs",
                    content = @Content(schema = @Schema(implementation = ApprovedPackDto.class)))
            ,            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token")

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

    @Operation(
            summary = "Update an existing offer",
            description = "Allows a delivery guy to update the details of an existing offer. The updated offer details are provided in the request body."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated offer",
                    content = @Content(schema = @Schema(implementation = OfferResDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid or incomplete data provided")
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

    @Operation(
            summary = "Mark a pack as delivered",
            description = "Allows a delivery guy to mark a specific package as delivered."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pack marked as delivered successfully",
                    content = @Content(schema = @Schema(implementation = ApprovedPackDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Pack with the specified ID does not exist"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token")

    })
    @PutMapping("/{id}/delivered")
    public ResponseEntity<ApprovedPackDto> deliver(
            @PathVariable UUID id,
            HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        Pack pack = deliveryGuyPackService.deliverPack(userId, id);
        return new ResponseEntity<>(approvedPackMapper.toApprovedPackDto(pack), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete an offer",
            description = "Allows a delivery guy to delete an existing offer."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found - Offer with the specified ID does not exist"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token")

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