package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.ApprovedPackDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.OfferDecisionRequest;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.PackResponseDto;
import com.sfar.livrili.Domains.Dto.ClientPackOfferDto.RattingRequestDto;
import com.sfar.livrili.Domains.Dto.ErrorDto.ApiErrorResponse;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Mapper.ApprovedPackMapper;
import com.sfar.livrili.Mapper.PacksForClientMapper;
import com.sfar.livrili.Service.ClientPackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Client Pack Management", description = "Endpoints for managing client packages, including creation, retrieval, updates, and deletion.")
public class ClientPackController {

    private final ClientPackService clientPackService;
    private final PacksForClientMapper packsForClientMapper;
    private final ApprovedPackMapper approvedPackMapper;

    @PostMapping
    @Operation(summary = "Create a new package", description = "Allows a client to create a new package. The package details are provided in the request body.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Package created successfully",
                    content = @Content(schema = @Schema(implementation = Pack.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Invalid or incomplete data provided",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Pack> addPack(@RequestBody PackRequestDto packRequestDto, HttpServletRequest request) {
        UUID uuid = (UUID) request.getAttribute("userId");
        return new ResponseEntity<>(clientPackService.createPackForClient(uuid, packRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Retrieve all packages", description = "Fetches a list of all packages associated with the authenticated client.")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token"),
            @ApiResponse(responseCode = "200", description = "Packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PackResponseDto.class)))

    })

    public ResponseEntity<List<PackResponseDto>> getAllPacks(HttpServletRequest request) {
        UUID uuid = (UUID) request.getAttribute("userId");
        List<Pack> packs = clientPackService.getAllPacks(uuid);
        List<PackResponseDto> packsResponse = packs.stream().map(packsForClientMapper::ToPackResponseDto).toList();
        return new ResponseEntity<>(packsResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a package", description = "Updates the details of an existing package identified by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Package updated successfully",
                    content = @Content(schema = @Schema(implementation = PackResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Package with the specified ID does not exist",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Invalid or incomplete data provided",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "Packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PackResponseDto.class)))
    })
    public ResponseEntity<PackResponseDto> updatePack(@PathVariable UUID id, @RequestBody PackRequestDto request, HttpServletRequest httpServletRequest) {
        UUID clientId = (UUID) httpServletRequest.getAttribute("userId");
        return new ResponseEntity<>(clientPackService.modifyPack(clientId, request, id), HttpStatus.OK);
    }

    @PutMapping("/{id}/rate")
    @Operation(summary = "Rate a delivered package", description = "Allows a client to confirm delivery and rate a package. The rating details are provided in the request body.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Package rated successfully",
                    content = @Content(schema = @Schema(implementation = ApprovedPackDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid rating data provided",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "Packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PackResponseDto.class)))
    })
    public ResponseEntity<ApprovedPackDto> confirmAndRateDelivered(@PathVariable UUID id, HttpServletRequest request, @RequestBody RattingRequestDto rattingRequestDto) {
        UUID clientId = (UUID) request.getAttribute("userId");
        Pack pack = clientPackService.giveRatting(clientId, id, rattingRequestDto);
        return new ResponseEntity<>(approvedPackMapper.toApprovedPackDto(pack), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a package", description = "Deletes a package identified by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Package deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found - Package with the specified ID does not exist",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "Packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PackResponseDto.class)))
    })
    public ResponseEntity<String> deletePack(@PathVariable UUID id, HttpServletRequest request) {
        UUID clientId = (UUID) request.getAttribute("userId");
        clientPackService.deletePack(clientId, id);
        return new ResponseEntity<>("The package has been deleted", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/offer/{id}/decision")
    @Operation(summary = "Approve or decline an offer", description = "Allows a client to approve or decline an offer related to a package. The decision is provided in the request body.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PackResponseDto.class))),
            @ApiResponse(responseCode = "200", description = "Decision applied successfully",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })

    public ResponseEntity<String> applyDecision(@PathVariable UUID id, HttpServletRequest request, @RequestBody OfferDecisionRequest offerDecisionRequest) {
        UUID clientId = (UUID) request.getAttribute("userId");
        return new ResponseEntity<>(clientPackService.approvePackOrDeclineOffer(clientId, id, offerDecisionRequest), HttpStatus.OK);
    }

    @GetMapping("/approved")
    @Operation(summary = "Retrieve approved packages", description = "Fetches a list of all packages that have been approved by the client.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Decision applied successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "200", description = "Approved packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApprovedPackDto.class)))
    })

    public ResponseEntity<List<ApprovedPackDto>> getApprovedPacks(HttpServletRequest request) {
        UUID uuid = (UUID) request.getAttribute("userId");
        List<Pack> packs = clientPackService.getApprovedPacks(uuid);
        List<ApprovedPackDto> approvedPacks = packs.stream().map(approvedPackMapper::toApprovedPackDto).toList();
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);
    }

    @GetMapping("/delivered")
    @Operation(summary = "Retrieve delivered packages", description = "Fetches a list of all packages that have been delivered to the client.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Approved packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApprovedPackDto.class))),
            @ApiResponse(responseCode = "200", description = "Delivered packages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApprovedPackDto.class)))
    })

    public ResponseEntity<List<ApprovedPackDto>> getDeliveredPacks(HttpServletRequest request) {
        UUID uuid = (UUID) request.getAttribute("userId");
        List<Pack> packs = clientPackService.getDeliveredPacks(uuid);
        List<ApprovedPackDto> approvedPacks = packs.stream().map(approvedPackMapper::toApprovedPackDto).toList();
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);
    }
}