package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.Client;
import com.sfar.livrili.Domains.Entities.Pack;
import com.sfar.livrili.Domains.Entities.PackageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackRepository extends JpaRepository<Pack, UUID> {

    @Query("SELECT p FROM Pack p WHERE p.client.id = :clientId")
    List<Pack> findAllByClientId(@Param("clientId") UUID userId);
    @Query("SELECT p FROM Pack p where p.client.id = :clientId and p.id = :packId")
   Optional<Pack> findByClientIdAndId(@Param("clientId") UUID userId, @Param("packId") UUID Id);

    @Query("SELECT p.status FROM Pack p where p.client.id = :clientId and p.id = :packId")
    Optional<String> findPackStatusByClientIdAndPackId(@Param("clientId") UUID userId, @Param("packId")UUID Id);

    @Modifying
    @Query("DELETE FROM Pack p WHERE p.client.id = :clientId AND p.id = :packId")
    void deleteByClientIdAndId(@Param("clientId") UUID clientId, @Param("packId") UUID id);


    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Pack p LEFT JOIN p.offers o WHERE p.id = :id")
    boolean existsPackWithOffers(@Param("id") UUID id);

    @Query("select p from  Pack p where p.id =:id")
    Optional<Pack> findPackById(@Param("id") UUID id);

    @Query("SELECT p FROM Pack p WHERE p.status = :status AND p.client.id = :clientId")
    Optional<List<Pack>> getApprovedPacksByClientId(@Param("clientId") UUID clientId, @Param("status") PackageStatus status);

    @Query("SELECT p FROM Pack p WHERE p.status = :status")
    Optional<List<Pack>> getApprovedPacks( @Param("status") PackageStatus status);









}
