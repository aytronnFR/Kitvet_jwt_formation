package com.aytronn.kibvet.repository;

import com.aytronn.kibvet.dao.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<LocalUser, UUID> {

    Optional<LocalUser> findByUsername(String username);
}
