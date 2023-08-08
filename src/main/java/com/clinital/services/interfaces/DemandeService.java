package com.clinital.services.interfaces;

import com.clinital.dto.DemandeDTO;
import com.clinital.enums.DemandeStateEnum;
import com.clinital.models.Demande;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;


public interface DemandeService {
	
	ResponseEntity<?> create(DemandeDTO demande);
	
	DemandeDTO update(DemandeDTO demande,Long id) throws Exception;
	
	List<DemandeDTO> findAll();
	
	DemandeDTO findById(Long id) throws Exception;
	
	void deleteById(Long id) throws Exception;

	Demande validate(DemandeStateEnum valide,Long id) throws Exception;

    List<Demande> findByState(DemandeStateEnum state);

}
