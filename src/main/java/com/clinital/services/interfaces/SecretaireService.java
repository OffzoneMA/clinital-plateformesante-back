package com.clinital.services.interfaces;

import java.util.List;

import com.clinital.dto.SecretaireDTO;
import com.clinital.models.Secretaire;
import com.clinital.payload.request.SecritaireRequest;

public interface SecretaireService {
	
	 Secretaire create(SecritaireRequest dto);
	
	 Secretaire update(SecritaireRequest dto, Long id) throws Exception;
	
	 List<Secretaire> findAll();
	
	 Secretaire findById(Long id) throws Exception;

	 boolean deleteSecretaireById(Long id,long cabinet) throws Exception;


}
