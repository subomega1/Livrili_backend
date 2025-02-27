package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.PackRequestDto;
import com.sfar.livrili.Domains.Dto.PackResponseDto;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Service.PackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/client/packs")
@RequiredArgsConstructor
public class ClientPackController {

    private final PackService packService;

    @PostMapping
    public Pack addPack(@RequestBody PackRequestDto packRequestDto , HttpServletRequest httpServletRequest) {
        UUID uuid = (UUID) httpServletRequest.getAttribute("userId");
        return packService.CreatePackForClient(uuid, packRequestDto);



    }
    @GetMapping
    public List<PackResponseDto> getAllPacks(HttpServletRequest httpServletRequest) {
        UUID uuid = (UUID) httpServletRequest.getAttribute("userId");
        return packService.GetAllPacks(uuid);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackResponseDto> updatePack(@PathVariable UUID id, @RequestBody PackRequestDto request, HttpServletRequest httpServletRequest) {
        UUID clientId = (UUID) httpServletRequest.getAttribute("userId");
        return new ResponseEntity<>( packService.modifyPack(clientId ,request,id), HttpStatus.OK);
    }

}
