package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinital.models.MotifConsultation;
@Repository
public interface MotifCondultationRepository extends JpaRepository<MotifConsultation,Long> {
    

    @Query(value ="SELECT * FROM motifs_consultation WHERE id = :id",nativeQuery = true)
	MotifConsultation getById(Long id); 
}
