package com.clinital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinital.models.Ville;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long>{
	
	
	public List<Ville> findAll();

}
