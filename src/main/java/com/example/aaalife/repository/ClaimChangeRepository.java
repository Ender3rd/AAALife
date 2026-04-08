package com.example.aaalife.repository;

import com.example.aaalife.model.ClaimChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimChangeRepository extends JpaRepository<ClaimChange, Long> {
}