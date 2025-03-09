package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.ApprovedPackDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.OfferDecisionRequest;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackResponseDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.RattingRequestDto;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Mapper.ApprovedPackMapper;
import com.sfar.livrili.Mapper.PacksForClientMapper;
import com.sfar.livrili.Service.ClientPackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/client/pack")
@RequiredArgsConstructor
@Tag(name = "Client Pack Management", description = "Operations related to client packages")
public class ClientPackController {

    private final ClientPackService clientPackService;
    private final PacksForClientMapper packsForClientMapper;
    private final ApprovedPackMapper approvedPackMapper;

    @PostMapping
    @Operation(summary = "Add a new package", description = "Creates a new package for a client.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Package created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Pack> addPack(@RequestBody PackRequestDto packRequestDto, HttpServletRequest request) {
        UUID uuid = (UUID) request.getAttribute("userId");
        return new ResponseEntity<>(clientPackService.createPackForClient(uuid, packRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all packages", description = "Retrieves all packages belonging to a client.")
    @ApiResponse(responseCode = "200", description = "Packages retrieved successfully")
    public ResponseEntity<List<PackResponseDto>> getAllPacks(HttpServletRequest request) {
        UUID uuid = (UUID) request.getAttribute("userId");
        List<Pack> packs = clientPackService.getAllPacks(uuid);
        List<PackResponseDto> packsResponse = packs.stream().map(packsForClientMapper::ToPackResponseDto).toList();
        return new ResponseEntity<>(packsResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a package", description = "Updates package details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Package updated successfully"),
            @ApiResponse(responseCode = "404", description = "Package not found")
    })
    public ResponseEntity<PackResponseDto> updatePack(@PathVariable UUID id, @RequestBody PackRequestDto request, HttpServletRequest httpServletRequest) {
        UUID clientId = (UUID) httpServletRequest.getAttribute("userId");
        return new ResponseEntity<>(clientPackService.modifyPack(clientId, request, id), HttpStatus.OK);
    }

    @PutMapping("/{id}/rate")
    @Operation(summary = "Confirm and rate a delivered package", description = "Allows a client to confirm and rate a package that has been delivered.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Package rated successfully"),
            @ApiResponse(responseCode = "404", description = "Package not found")
    })
    public ResponseEntity<ApprovedPackDto> confirmAndRateDelivered(@PathVariable UUID id, HttpServletRequest request, @RequestBody RattingRequestDto rattingRequestDto) {
        UUID clientId = (UUID) request.getAttribute("userId");
        Pack pack = clientPackService.giveRatting(clientId, id, rattingRequestDto);
        return new ResponseEntity<>(approvedPackMapper.toApprovedPackDto(pack), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a package", description = "Deletes a package by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Package deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Package not found")
    })
    public ResponseEntity<String> deletePack(@PathVariable UUID id, HttpServletRequest request) {
        UUID clientId = (UUID) request.getAttribute("userId");
        clientPackService.deletePack(clientId, id);
        return new ResponseEntity<>("The package has been deleted", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/offer/{id}/decision")
    @Operation(summary = "Approve or decline an offer", description = "Allows a client to approve or decline an offer related to a package.")
    @ApiResponse(responseCode = "200", description = "Decision applied successfully")
    public ResponseEntity<String> applyDecision(@PathVariable UUID id, HttpServletRequest request, @RequestBody OfferDecisionRequest offerDecisionRequest) {
        UUID clientId = (UUID) request.getAttribute("userId");
        return new ResponseEntity<>(clientPackService.approvePackOrDeclineOffer(clientId, id, offerDecisionRequest), HttpStatus.OK);
    }

    @GetMapping("/approved")
    @Operation(summary = "Get approved packages", description = "Retrieves all packages that have been approved.")
    @ApiResponse(responseCode = "200", description = "Approved packages retrieved successfully")
    public ResponseEntity<List<ApprovedPackDto>> getApprovedPacks(HttpServletRequest request) {
        UUID uuid = (UUID) request.getAttribute("userId");
        List<Pack> packs = clientPackService.getApprovedPacks(uuid);
        List<ApprovedPackDto> approvedPacks = packs.stream().map(approvedPackMapper::toApprovedPackDto).toList();
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);
    }

    @GetMapping("/delivered")
    @Operation(summary = "Get delivered packages", description = "Retrieves all packages that have been delivered.")
    @ApiResponse(responseCode = "200", description = "Delivered packages retrieved successfully")
    public ResponseEntity<List<ApprovedPackDto>> getDeliveredPacks(HttpServletRequest request) {
        UUID uuid = (UUID) request.getAttribute("userId");
        List<Pack> packs = clientPackService.getDeliveredPacks(uuid);
        List<ApprovedPackDto> approvedPacks = packs.stream().map(approvedPackMapper::toApprovedPackDto).toList();
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);
    }
}
