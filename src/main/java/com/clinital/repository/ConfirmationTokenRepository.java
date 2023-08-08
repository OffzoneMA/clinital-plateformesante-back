package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinital.security.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository 
            extends JpaRepository<ConfirmationToken, Long> {
    
    ConfirmationToken findByConfirmationToken(String token); 

    @Query(value = "", nativeQuery = true)
    ConfirmationToken getConfirmationTokenByUserId(long id);
}