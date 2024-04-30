package com.aytronn.kibvet.repository;

import com.aytronn.kibvet.dao.Kibble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KibbleRepository extends JpaRepository<Kibble, UUID> {

    @Query(value = """
            SELECT * FROM kibble
            WHERE id = ?1
            """,
            nativeQuery = true
    )
    Optional<Kibble> findByKibbleId(UUID id);
}
