package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Service.DeliveryGuyPackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/DG/pack")
@RequiredArgsConstructor
public class DeliveryGuyPackController {

    private final DeliveryGuyPackService deliveryGuyPackService;

    @GetMapping
    ResponseEntity<List<Pack>> getAllPacks() {
        return new ResponseEntity<>(deliveryGuyPackService.getPacks(), HttpStatus.OK);

    }
}
