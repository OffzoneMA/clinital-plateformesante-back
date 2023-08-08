package com.clinital.services.interfaces;

import java.util.List;
import com.clinital.dto.AllergiesDTO;
import com.clinital.models.Allergies;
import com.clinital.payload.request.AllergiesRequest;

public interface AllergiesServices{

    Allergies create(AllergiesRequest req) throws Exception;
	
    AllergiesDTO update(AllergiesRequest req, Long id) throws Exception;
   
    List<AllergiesDTO> findAll();
   
    AllergiesDTO findById(Long id) throws Exception;


}