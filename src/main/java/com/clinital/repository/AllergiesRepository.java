package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinital.models.Allergies;

@Repository
public interface AllergiesRepository extends JpaRepository<Allergies,Long> {
    
}
