package com.example.demo.repository;

import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByUsername(String username);

    UserDetails findByEmail(String email);

    Boolean existsByEmail(String username);
}
