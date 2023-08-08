package com.clinital.services.interfaces;

import java.util.List;

import com.clinital.dto.ConsultationDTO;
import com.clinital.models.Consultation;
import com.clinital.models.Medecin;
import com.clinital.payload.request.ConsultationRequest;

public interface ConsultaionService {

    Consultation create(ConsultationRequest consultationRequest,Medecin med) throws Exception;
	
	 ConsultationDTO update(ConsultationRequest req,Medecin med) throws Exception;
	
	 List<ConsultationDTO> findAll() throws Exception;
	
	 ConsultationDTO findById(Long id) throws Exception;

	 List<Consultation> findByIdName(String name) throws Exception;

	 Boolean deleteById(Consultation consultation) throws Exception;
    
}
