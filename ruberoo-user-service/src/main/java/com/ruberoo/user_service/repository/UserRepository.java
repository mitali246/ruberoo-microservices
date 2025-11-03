package com.ruberoo.user_service.repository;

import com.ruberoo.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Finder used by AuthController
    Optional<User> findByEmail(String email);
}
