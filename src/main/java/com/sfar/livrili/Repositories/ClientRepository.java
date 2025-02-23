package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
