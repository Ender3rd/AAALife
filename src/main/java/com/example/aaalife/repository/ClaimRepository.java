package com.example.aaalife.repository;

import com.example.aaalife.model.Claim;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    @Query("SELECT c FROM Claim c WHERE c.createdAt >= :since AND c.createdAt < :before GROUP BY c.policy, c.incidentDate HAVING COUNT(c) > 4")
    Optional<List<Claim>> findDuplicatesBetween(Instant sinceTimestamp, Instant beforeTimestamp);
}