package com.sfar.livrili.Repositories;

import com.sfar.livrili.Domains.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String Email);
    User findByEmail(String email);
    boolean existsByPhone(String phone);

}
