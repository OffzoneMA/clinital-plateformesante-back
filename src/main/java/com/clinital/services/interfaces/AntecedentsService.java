package com.clinital.services.interfaces;

import java.util.List;

import com.clinital.dto.AntecedentsDTO;
import com.clinital.models.Antecedents;
import com.clinital.models.Medecin;
import com.clinital.payload.request.AntecedentsRequest;


public interface AntecedentsService {

    Antecedents create(AntecedentsRequest AntecedentsRequest,Medecin med) throws Exception;
	
	 Antecedents update(AntecedentsRequest req,Medecin med) throws Exception;
	
	 List<AntecedentsDTO> findAll() throws Exception;
	
	 AntecedentsDTO findById(Long id) throws Exception;

	 Boolean deleteAntecedent(Antecedents Antecedents) throws Exception;
    
}
