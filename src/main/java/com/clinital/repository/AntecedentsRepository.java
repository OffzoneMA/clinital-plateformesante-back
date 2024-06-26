package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.models.Antecedents;

@Repository
public interface AntecedentsRepository extends JpaRepository<Antecedents,Long> {


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `antecedents` c WHERE c.id_antecedent=?1",nativeQuery = true)
    public static boolean deleteAntecedent(Long idantecedent){
        return true;
    };

    
}
