package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, UUID> {
    boolean existsById(UUID id);
}
