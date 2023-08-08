package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinital.models.ModeConsultation;

@Repository
public interface ModeConsultRespository extends JpaRepository<ModeConsultation,Long> {

    @Query(value ="SELECT * FROM mode_consultation WHERE id = :id",nativeQuery = true)
	ModeConsultation getById(Long id); 
}
