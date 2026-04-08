package com.example.aaalife.repository;

import com.example.aaalife.model.Claim;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Optional<List<Claim>> findByUserAndCreatedGreaterThan(Long userId, java.time.Instant since);

    Optional<List<Claim>> findDuplicatesBetween(Instant sinceTimestamp, Instant beforeTimestamp);
}