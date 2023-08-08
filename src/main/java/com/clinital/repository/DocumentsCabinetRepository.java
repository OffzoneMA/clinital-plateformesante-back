package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinital.models.DocumentsCabinet;

@Repository
public interface DocumentsCabinetRepository extends JpaRepository<DocumentsCabinet,Long>{
    
}
