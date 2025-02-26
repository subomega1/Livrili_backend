package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PackRepository extends JpaRepository<Pack, UUID> {

    @Query("SELECT p FROM Pack p WHERE p.client.id = :clientId")
    List<Pack> findAllByClientId(@Param("clientId") UUID userId);
}
