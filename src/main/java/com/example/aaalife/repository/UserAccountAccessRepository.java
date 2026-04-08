package com.example.aaalife.repository;

import com.example.aaalife.model.UserAccountAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountAccessRepository extends JpaRepository<UserAccountAccess, Long> {
}