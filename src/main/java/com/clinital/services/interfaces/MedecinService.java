package com.clinital.services.interfaces;

import java.util.List;

import com.clinital.dto.MedecinDTO;
import com.clinital.dto.PatientDTO;
import com.clinital.models.Medecin;
import com.clinital.models.Patient;
import com.clinital.payload.request.MedecinRequest;

public interface MedecinService {
	
	MedecinDTO create(MedecinRequest dto) throws Exception;
	
	 MedecinDTO update(MedecinRequest dto, Long id) throws Exception;
	
	 List<MedecinDTO> findAll();
	
	 Medecin findById(Long id) throws Exception;

	 void deleteById(Long id) throws Exception;
	 
	List<PatientDTO> getMedecinPatients(Long id) throws Exception;

	PatientDTO getPatient(Long idmed, long idpat) throws Exception;
	Medecin getMedecinByUserId(long id) throws Exception;


}
