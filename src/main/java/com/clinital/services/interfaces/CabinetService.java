package com.clinital.services.interfaces;

import java.util.List;

import com.clinital.dto.CabinetDTO;
import com.clinital.models.Cabinet;
import com.clinital.models.Medecin;
import com.clinital.payload.request.CabinetRequest;

public interface CabinetService {

	Cabinet create(CabinetRequest cabinetDTO,Medecin med) throws Exception;
	
	 CabinetDTO update(CabinetDTO dto, Long id) throws Exception;
	
	 List<CabinetDTO> findAll();
	
	 CabinetDTO findById(Long id) throws Exception;

	 List<Cabinet> findByIdName(String name) throws Exception;

	 List<Cabinet> allCabinetsByMedID(Long id) throws Exception;

	 //void deleteById(Long id) throws Exception;
	
}
