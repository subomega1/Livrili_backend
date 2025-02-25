package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.PackRequestDto;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Service.PackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/packs")
@RequiredArgsConstructor
public class ClientPackController {

    private final PackService packService;

    @PostMapping
    public Pack addPack(@RequestBody PackRequestDto packRequestDto , HttpServletRequest httpServletRequest) {
        UUID uuid = (UUID) httpServletRequest.getAttribute("userId");
        return packService.CreatePackForClient(uuid, packRequestDto);



    }
}
