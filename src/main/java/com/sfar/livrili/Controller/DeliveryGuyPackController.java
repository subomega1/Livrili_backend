package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.OfferRequest;
import com.sfar.livrili.Domains.Entities.Offer;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Service.DeliveryGuyPackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/DG/pack")
@RequiredArgsConstructor
public class DeliveryGuyPackController {

    private final DeliveryGuyPackService deliveryGuyPackService;

    @GetMapping
    ResponseEntity<List<Pack>> getAllPacks() {
        return new ResponseEntity<>(deliveryGuyPackService.getPacks(), HttpStatus.OK);
    }
    @PostMapping("/offer/{id}")
    ResponseEntity<Offer>giveOffer(@PathVariable UUID id , @RequestBody OfferRequest offerRequest, HttpServletRequest httpServletRequest) {
        UUID userId = (UUID) httpServletRequest.getAttribute("userId");
        return new ResponseEntity<>(deliveryGuyPackService.CreateOffer(offerRequest,userId,id), HttpStatus.CREATED);
    }
}
