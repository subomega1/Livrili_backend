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
public class ClientPackController {

    private final ClientPackService clientPackService;
    private final PacksForClientMapper packsForClientMapper;
    private final ApprovedPackMapper approvedPackMapper;

    @PostMapping
    public ResponseEntity<Pack> addPack(@RequestBody PackRequestDto packRequestDto , HttpServletRequest httpServletRequest) {
        UUID uuid = (UUID) httpServletRequest.getAttribute("userId");
        return new  ResponseEntity<>(clientPackService.createPackForClient(uuid, packRequestDto),HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<PackResponseDto>> getAllPacks(HttpServletRequest httpServletRequest) {
        UUID uuid = (UUID) httpServletRequest.getAttribute("userId");
        List<Pack> packs = clientPackService.getAllPacks(uuid);
        List<PackResponseDto> packsResponse = packs.stream().map(packsForClientMapper::ToPackResponseDto).toList();
        return new ResponseEntity<>(packsResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackResponseDto> updatePack(@PathVariable UUID id, @RequestBody PackRequestDto request, HttpServletRequest httpServletRequest) {
        UUID clientId = (UUID) httpServletRequest.getAttribute("userId");
        return new ResponseEntity<>( clientPackService.modifyPack(clientId ,request,id), HttpStatus.OK);
    }
    @PutMapping("/{id}/rate")
    public ResponseEntity<ApprovedPackDto> confirmAndRateDelivered(@PathVariable UUID id, HttpServletRequest httpServletRequest,@RequestBody RattingRequestDto rattingRequestDto) {
        UUID clientId = (UUID) httpServletRequest.getAttribute("userId");
        Pack pack = clientPackService.giveRatting(clientId,id,rattingRequestDto);
        return new ResponseEntity<>(approvedPackMapper.toApprovedPackDto(pack), HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePack(@PathVariable UUID id, HttpServletRequest httpServletRequest) {
        UUID clientId = (UUID) httpServletRequest.getAttribute("userId");
        clientPackService.deletePack(clientId,id);
        return new ResponseEntity<>("the package has been deleted",HttpStatus.NO_CONTENT);

    }
    @PutMapping("/offer/{id}/decision")
    public ResponseEntity<String> applyDecision(@PathVariable UUID id, HttpServletRequest httpServletRequest, @RequestBody OfferDecisionRequest offerDecisionRequest) {
        UUID clientId = (UUID) httpServletRequest.getAttribute("userId");
        return  new ResponseEntity<>(clientPackService.approvePackOrDeclineOffer(clientId,id,offerDecisionRequest), HttpStatus.OK);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ApprovedPackDto>> getApprovedPacks(HttpServletRequest httpServletRequest) {
        UUID uuid = (UUID) httpServletRequest.getAttribute("userId");
        List<Pack> packs = clientPackService.getApprovedPacks(uuid);
        List<ApprovedPackDto> approvedPacks = packs.stream().map(approvedPackMapper::toApprovedPackDto).toList();
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);
    }
    @GetMapping("/delivered")
    public ResponseEntity<List<ApprovedPackDto>> getDeliveredPacks(HttpServletRequest httpServletRequest) {
        UUID uuid = (UUID) httpServletRequest.getAttribute("userId");
        List<Pack> packs = clientPackService.getDeliveredPacks(uuid);
        List<ApprovedPackDto> approvedPacks = packs.stream().map(approvedPackMapper::toApprovedPackDto).toList();
        return new ResponseEntity<>(approvedPacks, HttpStatus.OK);
    }

}
