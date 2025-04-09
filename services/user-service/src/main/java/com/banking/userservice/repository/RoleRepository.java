package com.banking.userservice.repository;

import com.banking.userservice.model.ERole;
import com.banking.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);

    Boolean existsByName(ERole name);
}
