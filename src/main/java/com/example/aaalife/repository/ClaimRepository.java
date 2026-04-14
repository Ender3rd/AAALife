package com.example.aaalife.repository;

import com.example.aaalife.model.Claim;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    @Query("SELECT c FROM Claim c WHERE c.incidentDate in (SELECT c2.incidentDate FROM Claim c2 GROUP BY c2.policy, c2.incidentDate HAVING COUNT(c2) > 4)")
    List<Claim> findDuplicatesBetween(Instant sinceTimestamp, Instant beforeTimestamp);

    List<Claim> findAllByPolicyId(Long policyId);
}
