package com.clinital.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinital.enums.DemandeStateEnum;
import com.clinital.models.Demande;

@Repository
public interface IDemandeRepository extends JpaRepository<Demande, Long>{

    
    @Query(value="SELECT d.* FROM demande d WHERE d.validation=?1",nativeQuery = true)
    List<Demande> getdemandeByState(String state);
    
    @Query(value="SELECT d.* FROM demande d WHERE d.id=?1",nativeQuery = true)
    Optional<Demande> findByIDemande(Long id);

    @Query(value="SELECT d.* FROM demande d WHERE d.id=?1",nativeQuery = true)
    Demande findByid(Long id);

}
