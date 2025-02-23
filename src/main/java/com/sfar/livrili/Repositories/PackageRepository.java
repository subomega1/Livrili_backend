package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.Pack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageRepository extends JpaRepository<Pack, UUID> {

}
