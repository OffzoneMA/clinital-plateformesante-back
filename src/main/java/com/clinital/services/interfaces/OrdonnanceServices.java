package com.clinital.services.interfaces;

import java.util.List;

import com.clinital.dto.OrdonnanceDTO;
import com.clinital.models.Medecin;
import com.clinital.models.Ordonnance;
import com.clinital.payload.request.OrdonnanceRequest;

public interface OrdonnanceServices {
    
    Ordonnance create(OrdonnanceRequest consultationRequest,Medecin med) throws Exception;
	
    OrdonnanceDTO update(OrdonnanceRequest req,Medecin med) throws Exception;
   
    List<OrdonnanceDTO> findAll() throws Exception;
   
    OrdonnanceDTO findById(Long id,Medecin med) throws Exception;

    List<Ordonnance> findByIdName(String name) throws Exception;

    Boolean deleteById(Ordonnance consultation) throws Exception;

}




