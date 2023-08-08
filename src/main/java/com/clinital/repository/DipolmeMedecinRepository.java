package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinital.models.DiplomeMedecin;


@Repository
public interface DipolmeMedecinRepository extends JpaRepository<DiplomeMedecin,Long> {
    
}
